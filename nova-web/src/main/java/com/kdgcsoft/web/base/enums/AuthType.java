package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-04-28 15:38
 */
@Dic("权限类型")
public enum AuthType implements IDic {
    MENU("菜单"),
    BUTTON("按钮");
    private String text;

    AuthType(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
