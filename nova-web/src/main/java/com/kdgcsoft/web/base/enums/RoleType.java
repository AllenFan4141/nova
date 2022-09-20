package com.kdgcsoft.web.base.enums;


import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * @author fyin
 * @date 2021-04-28 15:38
 */
@Dic("角色类型")
public enum RoleType implements IDic {
    ADMIN("管理员"),
    DEVOPS("运维人员"),
    USER("普通用户");
    private String text;

    RoleType(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
