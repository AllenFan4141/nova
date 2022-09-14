package com.kdgcsoft.web.module.interfaces;


import com.kdgcsoft.web.module.model.ModuleMenu;
import com.kdgcsoft.web.module.model.ModuleParam;

import java.util.List;

/**
 * @author fyin
 * @date 2021-05-12 11:01
 */
public interface IModule {
    /**
     * 模块名称
     *
     * @return
     */
    String name();

    /**
     * 模块编码
     *
     * @return
     */
    String code();

    /**
     * 模块顺序
     *
     * @return
     */
    int order();

    /**
     * 模块的描述信息
     *
     * @return
     */
    String description();

    /**
     * 模块的菜单
     *
     * @return
     */
    List<ModuleMenu> menus();

    /**
     * 模块参数
     *
     * @return
     */
    List<ModuleParam> params();

}
