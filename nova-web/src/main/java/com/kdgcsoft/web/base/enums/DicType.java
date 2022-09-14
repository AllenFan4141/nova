package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-05-11 14:26
 */
@Dic("字典类型")
public enum DicType implements IDic {
    /**
     */
    LIST("列表"),
    /**
     */
    TREE("树形");

    private String text;

    DicType(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
