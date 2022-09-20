package com.kdgcsoft.web.base.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Filter;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kdgcsoft.common.exception.BizException;
import com.kdgcsoft.common.util.TreeUtil;
import com.kdgcsoft.web.base.entity.BaseDic;
import com.kdgcsoft.web.base.entity.BaseDicItem;
import com.kdgcsoft.web.base.entity.BaseOrg;
import com.kdgcsoft.web.base.enums.DicType;
import com.kdgcsoft.web.base.enums.YesNo;
import com.kdgcsoft.web.base.mapper.BaseDicMapper;
import com.kdgcsoft.web.common.model.PageRequest;
import icu.mhb.mybatisplus.plugln.base.service.impl.JoinServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author YINFAN
 */
@Service
public class BaseDicService extends JoinServiceImpl<BaseDicMapper, BaseDic> {
    /**
     * 缓存名称
     */
    private static final String CACHE_NAME = "BaseDic";

    @Autowired
    BaseDicItemService baseDicItemService;


    public PageRequest pageDic(PageRequest page, String search) {
        page = baseMapper.selectPage(page, new QueryWrapper<BaseDic>().lambda()
                .like(StrUtil.isNotEmpty(search), BaseDic::getCode, search).or()
                .like(StrUtil.isNotEmpty(search), BaseDic::getName, search)
                .orderByDesc(BaseDic::getId));
        return page;
    }

    public BaseDic getDicByCode(String code) {
        BaseDic dic = baseMapper.selectOne(new LambdaQueryWrapper<BaseDic>().eq(BaseDic::getCode, code));
        if (dic != null) {
            List<BaseDicItem> items = baseDicItemService.listItemsByDicCode(code);
            switch (dic.getDicType()) {
                case TREE:
                    dic.setItems(TreeUtil.buildTree(items));
                    break;
                default:
                    dic.setItems(items);
            }
        }
        return dic;
    }

    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public BaseDic saveDic(BaseDic entity) {
        if (hasRepeat(entity)) {
            throw new BizException(StrUtil.format("字典编码[{}]已存在", entity.getCode()));
        }

        if (CollUtil.isNotEmpty(entity.getItems())) {
            for (BaseDicItem item : entity.getItems()) {
                if (baseDicItemService.hasRepeat(item)) {
                    throw new BizException(StrUtil.format("字典项值[{}]已存在", item.getValue()));
                }
            }
        }

        saveOrUpdate(entity);

        if (CollUtil.isNotEmpty(entity.getItems())) {
            List<BaseDicItem> items = entity.getItems();
            List<BaseDicItem> dbItems = baseDicItemService.listItemsByDicCode(entity.getCode());
            List<Long> removeIds = new ArrayList<>();
            List<BaseDicItem> updateItems = new ArrayList<>();
            CollUtil.forEach(dbItems, (CollUtil.Consumer<BaseDicItem>) (dbItem, index) -> {
                //如果数据库的字典项不在传入的参数里面,则删除
                if (!CollUtil.contains(items, item -> dbItem.getDicCode().equals(item.getDicCode()) && dbItem.getValue().equals(item.getValue()))) {
                    removeIds.add(dbItem.getId());
                }
            });

            CollUtil.forEach(items, (CollUtil.Consumer<BaseDicItem>) (item, index) -> {
                BaseDicItem dbItem = CollUtil.findOne(dbItems, dbItem1 -> dbItem1.getDicCode().equals(item.getDicCode()) && dbItem1.getValue().equals(item.getValue()));
                //数据库不存在或者数据库中的数据跟前台的数据不同 都加入更新列表
                if (dbItem == null || !dbItem.toString().equals(item.toString())) {
                    updateItems.add(item);
                }
            });

            if (CollUtil.isNotEmpty(removeIds)) {
                baseDicItemService.removeBatchByIds(removeIds);
            }

            if (CollUtil.isNotEmpty(updateItems)) {
                baseDicItemService.saveOrUpdateBatch(updateItems);
            }
        }
        return entity;
    }


    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = CACHE_NAME, allEntries = true)
    public void deleteDicByCode(String dicCode) {
        BaseDic dic = baseMapper.selectOne(new LambdaQueryWrapper<BaseDic>().eq(BaseDic::getCode, dicCode));
        if (dic == null) {
            throw new BizException("字典不存在");
        }

        if (dic.getEmbed() == YesNo.Y) {
            throw new BizException("内置字典不能删除");
        }

        baseMapper.deleteById(dic.getId());
        baseDicItemService.deleteByDicCode(dic.getCode());
    }

    /**
     * 检查是否有编码重复的数据
     *
     * @param entity
     * @return
     */
    public boolean hasRepeat(BaseDic entity) {
        return baseMapper.exists(new LambdaQueryWrapper<BaseDic>()
                .eq(BaseDic::getCode, entity.getCode())
                .ne(entity.getId() != null, BaseDic::getId, entity.getId())
        );
    }

    @Cacheable(value = CACHE_NAME, key = "#root.methodName+'.'+#dicCode")
    public Map<String, String> dicValueMap(String dicCode) {
        List<BaseDicItem> items = baseDicItemService.listItemsByDicCode(dicCode);
        HashMap<String, String> map = MapUtil.newHashMap();
        items.forEach(baseDicItem -> map.put(baseDicItem.getValue(), baseDicItem.getText()));
        return map;
    }

    /**
     * 根据字典编码和值 获取字典文字
     *
     * @param dicCode
     * @param value
     * @return
     */
    public String getDicText(String dicCode, Object value) {
        if (value == null) {
            return null;
        }
        //这里需要调用代理类提供的方法 而不是自己内部的方法 才能使缓存生效
        BaseDicService dicService = SpringUtil.getBean(BaseDicService.class);
        Map<String, String> dicValueMap = dicService.dicValueMap(dicCode);
        if (dicValueMap.containsKey(value.toString())) {
            return dicValueMap.get(value.toString());
        } else {
            return value.toString();
        }
    }
}