package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-05-11 14:39
 */
@Dic("性别")
public enum Gender implements IDic {
    /**
     * 男性
     */
    MALE("男性"),
    /**
     * 女性
     */
    FEMALE("女性"),
    /**
     * 其他
     */
    OTHER("其他");

    private String text;

    Gender(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}