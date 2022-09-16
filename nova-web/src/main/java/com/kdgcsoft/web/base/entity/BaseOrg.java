package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.common.interfaces.ITreeNode;
import com.kdgcsoft.web.base.enums.YesNo;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 组织机构
 *
 * @TableName base_org
 */
@TableName(value = "base_org")
@Data
public class BaseOrg extends BaseEntity implements Serializable, ITreeNode<BaseOrg> {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long orgId;

    /**
     * 上级组织ID
     */
    private Long orgPid;

    /**
     * 组织机构名称
     */
    private String orgName;

    /**
     * 组织机构编码
     */
    private String orgCode;

    /**
     * 组织机构全称
     */
    private String orgFullName;

    /**
     * 启用
     */
    private YesNo enabled;

    /**
     * 顺序号
     */
    private String orderNo;

    @TableField(exist = false)
    private List<BaseOrg> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public Object id() {
        return this.orgId;
    }

    @Override
    public Object pid() {
        return this.orgPid;
    }
}