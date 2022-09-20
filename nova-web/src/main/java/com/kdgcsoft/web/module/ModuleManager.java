package com.kdgcsoft.web.module;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;
import com.kdgcsoft.web.base.entity.BaseDic;
import com.kdgcsoft.web.base.entity.BaseDicItem;
import com.kdgcsoft.web.base.entity.BaseMenu;
import com.kdgcsoft.web.base.entity.BaseParam;
import com.kdgcsoft.web.base.enums.DicType;
import com.kdgcsoft.web.base.enums.MenuOpenType;
import com.kdgcsoft.web.base.enums.YesNo;
import com.kdgcsoft.web.base.service.BaseDicItemService;
import com.kdgcsoft.web.base.service.BaseDicService;
import com.kdgcsoft.web.base.service.BaseMenuService;
import com.kdgcsoft.web.base.service.BaseParamService;
import com.kdgcsoft.web.config.NovaProperties;
import com.kdgcsoft.web.module.event.BeforeModuleLoadEvent;
import com.kdgcsoft.web.module.event.ModuleLoadedEvent;
import com.kdgcsoft.web.module.event.ModuleManagerInitedEvent;
import com.kdgcsoft.web.module.exception.ModuleException;
import com.kdgcsoft.web.module.interfaces.IModule;
import com.kdgcsoft.web.module.model.Module;
import com.kdgcsoft.web.module.model.ModuleDic;
import com.kdgcsoft.web.module.model.ModuleMenu;
import com.kdgcsoft.web.module.model.ModuleParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 模块管理器,主要负责在启动时扫描模块配置文件来进行菜单,字典,参数的初始化和数据库同步
 *
 * @author fyin
 * @date 2022年09月01日 16:58
 */
@Slf4j
public class ModuleManager {
    /**
     * 模块文件的扫描地址
     */
    public static final String MODULE_RESOURCE = "classpath*:nova-module.json";
    private final PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
    private ConfigurableApplicationContext applicationContext;
    /**
     * ModuleManager是否初始化完成
     */
    private boolean inited = false;
    private NovaProperties novaProperties;

    private BaseParamService baseParamService;

    private BaseDicService baseDicService;
    private BaseDicItemService baseDicItemService;

    private BaseMenuService baseMenuService;

    /**
     * 模块列表
     **/
    private List<Module> modules = new ArrayList<>();

    /**
     * 模块内置字典列表
     **/
    private List<ModuleDic> dicList = new ArrayList<>();
    /**
     * 模块内置参数列表
     **/
    private List<ModuleParam> moduleParamList = new ArrayList<>();

    /**
     * 参数的map key为 moduleCode.paramCode 如 base.WEB_EY
     */
    private final Map<String, BaseParam> paramsMap = new HashMap<>();


    public ModuleManager(ConfigurableApplicationContext applicationContext, NovaProperties novaProperties) {
        this.applicationContext = applicationContext;
        this.novaProperties = novaProperties;
        this.baseParamService = applicationContext.getBean(BaseParamService.class);
        this.baseDicService = applicationContext.getBean(BaseDicService.class);
        this.baseDicItemService = applicationContext.getBean(BaseDicItemService.class);
        this.baseMenuService = applicationContext.getBean(BaseMenuService.class);
    }

    /**
     * ModuleManager初始化方法
     */
    private void init() {
        log.info("ModuleManager initialization start.");
        this.inited = false;
        log.info("Start load Modules.");
        log.info("publish Event:{} ", BeforeModuleLoadEvent.class.getSimpleName());
        applicationContext.publishEvent(new BeforeModuleLoadEvent(this));
        loadModules();
        loadDics();
        syncModuleDetails();
        refreshParamsMap();
        this.inited = true;
        log.info("ModuleManager initialization complete.");
        applicationContext.publishEvent(new ModuleManagerInitedEvent(this));
    }

    /**
     * 扫描并加载所有的modules
     */
    private void loadModules() {
        log.info("Scanning Modules...");
        List<Module> list = new ArrayList<>();
        list.addAll(scanModuleFormAnno());
        list.addAll(scanModuleFromJson());


        list.forEach(module -> {
            if (existModule(module.getCode())) {
                throw new ModuleException(StrUtil.format("Repeated Module[code='{}','name'='{}']", module.getCode(), module.getName()));
            } else {
                log.info("Loaded Module[code='{}','name'='{}'].", module.getCode(), module.getName());
                modules.add(module);
            }
        });
        //排序
        this.modules = CollUtil.sort(this.modules, Comparator.comparing(Module::getOrder));
        log.info("Scanned {} Modules.", modules.size());

        log.info("publish Event [{}].", ModuleLoadedEvent.class.getSimpleName());
        //发布模块加载完成事件 ,提供事件机制可以让其他模块在此介入
        applicationContext.publishEvent(new ModuleLoadedEvent(this, modules));

    }


    /**
     * 加载字典
     */
    private void loadDics() {
        log.debug("Scanning ModuleDic from @{} implements IDic ...", Dic.class.getSimpleName());
        ClassUtil.scanPackageByAnnotation("com.kdgcsoft", Dic.class).forEach(cls -> {
            if (IDic.class.isAssignableFrom(cls) && cls.isEnum()) {
                Dic ann = cls.getAnnotation(Dic.class);
                if (ann != null) {
                    String code = StrUtil.isEmpty(ann.code()) ? cls.getSimpleName() : ann.code();
                    if (existDic(code)) {
                        throw new ModuleException(StrUtil.format("Repeated ModuleDic code '{}' from {}", code, ann.getClass().getName()));
                    }
                    String name = StrUtil.isEmpty(ann.value()) ? cls.getSimpleName() : ann.value();
                    String description = StrUtil.isEmpty(ann.description()) ? cls.getName() : ann.description();
                    ModuleDic mDic = new ModuleDic().setCode(code).setName(name).setDescription(description);
                    for (Object a : cls.getEnumConstants()) {
                        IDic i = (IDic) a;
                        Enum e = (Enum) a;
                        mDic.addOption(e.name(), i.text());
                    }
                    dicList.add(mDic);
                    log.debug("Loaded Dic[code='{}',name='{}'] from {}", mDic.getCode(), mDic.getName(), cls.getName());
                } else {
                    log.warn("Scanned ModuleDic from [{}],please use anno [@{}] to enabled it.", cls.getName(), Dic.class.getName());
                }

            }
        });
        log.info("Scanned {} ModuleDic.", dicList.size());
    }


    /**
     * 同步模块内的其他相关信息
     */
    private void syncModuleDetails() {
        syncParams();
        syncDic();
        syncMenus();
    }

    /**
     * 同步模块参数到数据库
     */
    private void syncParams() {
        log.debug("Sync Params to database...");
        List<BaseParam> allDbParams = this.baseParamService.list();
        List<BaseParam> saveParamList = new ArrayList<>();
        this.modules.forEach(m -> {
            List<ModuleParam> params = m.getParams();
            if (CollUtil.isNotEmpty(params)) {
                params.forEach(p -> {
                    this.moduleParamList.add(p);
                    //根据模块编码和参数编码来查找已经存在的参数 存储的时候是以moduleCode+"."+paramCode来存储的
                    BaseParam dbParam = CollUtil.findOne(allDbParams, dbp -> dbp.getCode().equals(m.getCode() + StrUtil.DOT + p.getCode()));
                    if (dbParam == null) {
                        //不存在则创建新的
                        dbParam = new BaseParam();
                        dbParam.setModuleCode(m.getCode());
                        dbParam.setName(p.getName());
                        dbParam.setCode(m.getCode() + StrUtil.DOT + p.getCode());
                        dbParam.setEmbed(YesNo.Y);
                        dbParam.setAnonAccess(YesNo.ofBoolean(p.isAnonAccess()));
                        dbParam.setParamType(p.getType());
                        dbParam.setDefaultValue(p.getDefaultValue());
                        dbParam.setValue(p.getDefaultValue());
                        dbParam.setDescription(p.getDescription());
                        saveParamList.add(dbParam);
                    } else {
                        if (!p.sameToDb(dbParam)) {
                            //存在则更新名称,默认值,描述

                            //如果数据库中参数的值和默认值是一样的时候  如果默认值发生了变化 也要同步更新value为默认值
                            if (StrUtil.equals(dbParam.getDefaultValue(), dbParam.getValue())) {
                                dbParam.setValue(p.getDefaultValue());
                            }
                            dbParam.setName(p.getName());
                            dbParam.setParamType(p.getType());
                            dbParam.setAnonAccess(YesNo.ofBoolean(p.isAnonAccess()));
                            dbParam.setDefaultValue(p.getDefaultValue());
                            dbParam.setDescription(p.getDescription());
                            saveParamList.add(dbParam);
                        }
                    }

                });
            }
        });
        this.baseParamService.saveOrUpdateBatch(saveParamList);
        log.info("Sync Params finished.{} Params updated.", saveParamList.size());
    }


    /**
     * 同步字典数据,字典是扫描来的 只支持LIST格式
     */
    private void syncDic() {
        log.debug("Sync Dic to database.");
        List<BaseDic> dbDics = baseDicService.list();
        List<BaseDicItem> dbDicItems = baseDicItemService.list();
        List<BaseDic> saveDicList = new ArrayList<>();
        List<BaseDicItem> saveDicItemList = new ArrayList<>();
        this.dicList.forEach(d -> {
            BaseDic dbDic = CollUtil.findOne(dbDics, dic -> dic.getCode().equals(d.getCode()));
            if (dbDic == null) {
                dbDic = new BaseDic();
                dbDic.setCode(d.getCode());
                dbDic.setName(d.getName());
                dbDic.setEmbed(YesNo.Y);
                dbDic.setDicType(DicType.LIST);
                dbDic.setMemo(d.getDescription());
                saveDicList.add(dbDic);
            } else {
                //已经存在的则不更新,因为除了编码,其他的属性前台都能提供修改,如果前台修改了就以前台修改过的为准,所以不需要更新
            }
            CollUtil.forEach(d.getOptions(), (o, index) -> {
                BaseDicItem dbDicItem = CollUtil.findOne(dbDicItems, i -> i.getDicCode().equals(d.getCode()) && i.getValue().equals(o.getKey()));
                if (dbDicItem == null) {
                    dbDicItem = new BaseDicItem();
                    dbDicItem.setPid(0L);
                    dbDicItem.setEmbed(YesNo.Y);
                    dbDicItem.setDicCode(d.getCode());
                    dbDicItem.setValue(o.getKey());
                    dbDicItem.setText(o.getValue());
                    dbDicItem.setOrderNo(index);
                    saveDicItemList.add(dbDicItem);
                }
            });
        });

        baseDicService.saveOrUpdateBatch(saveDicList);
        baseDicItemService.saveOrUpdateBatch(saveDicItemList);
        log.info("Sync Dic finished.{} dic updated,{} dicItem updated", saveDicList.size(), saveDicItemList.size());
    }


    private void syncMenus() {
        log.debug("Sync Menus to database.");
        List<BaseMenu> dbMenuList = baseMenuService.list();
        List<BaseMenu> saveMenuList = new ArrayList<>();
        this.modules.forEach(module -> {
            List<ModuleMenu> menus = module.getMenus();
            buildUpdateMenus(dbMenuList, saveMenuList, menus, "0");
        });
        baseMenuService.saveOrUpdateBatch(saveMenuList);
        log.info("Sync Menus finished.{} menus updated.", saveMenuList.size());
    }

    /**
     * 刷新参数列表,参数key是moduleCode+"."+paramCode
     */
    public void refreshParamsMap() {
        log.debug("Refresh Params from database.");
        this.paramsMap.clear();
        List<BaseParam> dbParams = baseParamService.list();
        dbParams.forEach(baseParam -> paramsMap.put(baseParam.getCode(), baseParam));
        log.info("Refresh Params from database.{} params loaded.", paramsMap.size());
    }

    /**
     * 遍历菜单,保存菜单
     *
     * @param allDbMenus   数据库菜单列表
     * @param saveMenuList 内存中要更新的菜单列表
     * @param moduleMenus  模块扫描到的所有的菜单
     * @param pMenuCode    上级菜单编码
     */
    private void buildUpdateMenus(List<BaseMenu> allDbMenus, List<BaseMenu> saveMenuList, List<ModuleMenu> moduleMenus, String pMenuCode) {
        CollUtil.forEach(moduleMenus, (menu, index) -> {
            BaseMenu one = CollUtil.findOne(allDbMenus, dbMenu -> dbMenu.getCode().equals(menu.getCode()));
            if (one == null) {
                one = new BaseMenu();
                one.setPcode(pMenuCode);
                one.setCode(menu.getCode());
                one.setName(menu.getName());
                one.setUrl(menu.getUrl());
                one.setEmbed(YesNo.Y);
                one.setEnabled(YesNo.Y);
                one.setOpenType(MenuOpenType.TAB);
                one.setOrderNo(index);
                saveMenuList.add(one);
            } else {
                //如果地址不同则以代码中指定的地址为准,因为是内置的菜单,不允许改地址,改地址了的话肯定是开发这边变更了菜单地址
                if (!StrUtil.equals(one.getUrl(), menu.getUrl())) {
                    one.setUrl(menu.getUrl());
                    saveMenuList.add(one);
                }
            }
            buildUpdateMenus(allDbMenus, saveMenuList, menu.getChildren(), menu.getCode());
        });
    }

    /**
     * 扫描使用注解声明的module
     *
     * @return List<Module>
     */
    private List<Module> scanModuleFormAnno() {
        List<Module> list = new ArrayList<>();
        log.debug("Scanning Modules from interface {}...", IModule.class.getSimpleName());
        Map<String, IModule> map = applicationContext.getBeansOfType(IModule.class);
        for (IModule iModule : map.values()) {
            Module module = new Module();
            module.setMenus(iModule.menus()).setParams(iModule.params()).setDescription(iModule.description()).setBaseUrl(ClassUtil.getLocation(iModule.getClass()));
            list.add(module);
        }
        log.debug("Scanned {} Modules from interface {}.", list.size(), IModule.class.getSimpleName());
        return list;
    }

    /**
     * 扫描使用json文件声明的module
     *
     * @return List<Module>
     */
    private List<Module> scanModuleFromJson() {
        List<Module> list = new ArrayList<>();
        log.debug("Scanning Modules from url pattern[{}]...", MODULE_RESOURCE);
        try {
            Resource[] resources = this.pathResolver.getResources(MODULE_RESOURCE);
            Arrays.asList(resources).forEach(res -> {
                Module module = parseJson2Module(res);
                list.add(module);
            });
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        log.debug("Scanned {} Modules from url pattern[{}].", list.size(), MODULE_RESOURCE);
        return list;
    }

    /**
     * 将JSON文件转化为javabean
     *
     * @param resource json文件
     * @return Module
     */
    private Module parseJson2Module(Resource resource) {
        Module module;
        try {

            module = JSON.parseObject(FileUtil.readString(resource.getURL(), StandardCharsets.UTF_8), Module.class);
            module.setBaseUrl(resource.getURL());
        } catch (Exception e) {
            log.error("Parse json to java Module error.", e);
            throw new ModuleException("模块文件解析出错", e);
        }
        return module;
    }

    /**
     * 有无重复编码的模块
     *
     * @param code 模块编码
     * @return 是否有重复
     */
    private boolean existModule(String code) {
        return CollUtil.findOne(modules, module -> module.getCode().equals(code)) != null;
    }

    /**
     * 判断有无重复编码的字典
     *
     * @param code 菜单编码
     * @return 是否有重复菜单
     */
    private boolean existDic(String code) {
        return CollUtil.findOne(dicList, dic -> dic.getCode().equals(code)) != null;
    }

    public Map<String, BaseParam> getParamsMap() {
        return this.paramsMap;
    }

    /**
     * 获得参数对象
     *
     * @param key key
     * @return BaseParam
     */
    public BaseParam getBaseParam(String key) {
        return this.paramsMap.get(key);
    }

    /**
     * 获得参数对应的值,文件会是文件ID,而不是地址
     *
     * @param key key
     * @return 参数值
     */
    public Object getBaseParamValue(String key) {
        if (this.paramsMap.containsKey(key)) {
            return this.paramsMap.get(key).getValue();
        } else {
            return null;
        }
    }

    /**
     * 获得参数的格式化后的值,图片/文件会格式化为访问地址,数值参数会格式化为数值
     *
     * @param key key
     * @return 格式化后的参数值
     */
    public Object getBaseParamFmtValue(String key) {
        if (this.paramsMap.containsKey(key)) {
            return this.paramsMap.get(key).getFmtValue();
        } else {
            return null;
        }
    }

    /**
     * 获取可匿名访问的[参数key-value]的map
     *
     * @return
     */
    public Map<String, Object> getAnonParamsValueMap() {
        Map<String, Object> map = MapUtil.newHashMap();
        for (String key : this.paramsMap.keySet()) {
            BaseParam param = this.paramsMap.get(key);
            if (param.getAnonAccess().isY()) {
                map.put(key, param.getValue());
            }
        }
        return map;
    }

    /**
     * 获取可匿名访问的[参数key-格式化后的值]的map
     *
     * @return
     */
    public Map<String, Object> getAnonParamsFmtValueMap() {
        Map<String, Object> map = MapUtil.newHashMap();
        for (String key : this.paramsMap.keySet()) {
            BaseParam param = this.paramsMap.get(key);
            if (param.getAnonAccess().isY()) {
                map.put(key, param.getValue());
            }
        }
        return map;
    }


    /**
     * 获得参数的默认值
     *
     * @param key key
     * @return 参数默认值
     */
    public Object getBaseParamDefaultValue(String key) {
        if (this.paramsMap.containsKey(key)) {
            return this.paramsMap.get(key).getDefaultValue();
        } else {
            return null;
        }
    }

    /**
     * 是否已经初始化完成
     *
     * @return boolean
     */
    public boolean isInited() {
        return inited;
    }
}
