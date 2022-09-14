package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-05-11 14:26
 */
@Dic("是否内置")
public enum Embed implements IDic {
    /**
     * 启用
     */
    Y("内置"),
    /**
     * 停用
     */
    N("非内置");

    private String text;

    Embed(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
