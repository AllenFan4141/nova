package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.web.base.enums.RoleType;
import com.kdgcsoft.web.base.enums.YesNo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色
 *
 * @TableName base_role
 */
@TableName(value = "base_role")
@Data
public class BaseRole extends BaseEntity implements Serializable {
    /**
     * 角色ID
     */
    @TableId(type = IdType.AUTO)
    private Long roleId;

    /**
     * 所属组织机构ID
     */
    private Long orgId;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色类型
     */
    private RoleType roleType;

    /**
     * 启用状态
     */
    private YesNo enabled;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}