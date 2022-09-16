package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-05-11 14:26
 */
@Dic("是否")
public enum YesNo implements IDic {
    /**
     * 启用
     */
    Y("是"),
    /**
     * 停用
     */
    N("否");

    private String text;

    YesNo(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }

    public static YesNo ofBoolean(boolean trueOrFalse) {
        return trueOrFalse ? YesNo.Y : YesNo.N;
    }

    public boolean isY() {
        return this.equals(YesNo.Y);
    }
}
