package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.web.base.enums.AuthType;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色权限
 *
 * @TableName base_role_auth
 */
@TableName(value = "base_role_auth")
@Data
public class BaseRoleAuth extends BaseEntity implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 角色ID
     */
    private Long roleId;

    /**
     * 权限编码
     */
    private String authCode;

    /**
     * 权限类型
     */
    private AuthType authType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}