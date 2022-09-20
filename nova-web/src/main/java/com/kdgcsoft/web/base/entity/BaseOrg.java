package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.common.interfaces.ITreeNode;
import com.kdgcsoft.web.base.enums.YesNo;
import com.kdgcsoft.web.common.consts.WebConst;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "上级组织机构ID不能为空")
    private Long orgPid = WebConst.DEF_TREE_ROOT_ID;

    /**
     * 组织机构名称
     */
    @NotBlank(message = "组织机构名称不能为空")
    private String orgName;

    /**
     * 组织机构编码
     */
    @NotBlank(message = "组织机构编码不能为空")
    private String orgCode;

    /**
     * 组织机构全称
     */
    private String orgFullName;

    /**
     * 启用
     */
    private YesNo enabled = YesNo.Y;

    /**
     * 顺序号
     */
    private Integer orderNo;

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