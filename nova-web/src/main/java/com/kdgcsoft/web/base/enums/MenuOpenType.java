package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-05-11 14:26
 */
@Dic("菜单打开方式")
public enum MenuOpenType implements IDic {
    /**
     *
     */
    TAB("新tab页"),
    /**
     *
     */
    WINDOW("新浏览器窗口"),
    /**
     *
     */
    JUMP("页面跳转");

    private String text;

    MenuOpenType(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
