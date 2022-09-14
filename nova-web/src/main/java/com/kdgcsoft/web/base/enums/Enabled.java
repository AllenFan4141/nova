package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-05-11 14:26
 */
@Dic("启用状态")
public enum Enabled implements IDic {
    /**
     * 启用
     */
    Y("启用"),
    /**
     * 停用
     */
    N("停用");

    private String text;

    Enabled(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
