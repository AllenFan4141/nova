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
 * @TableName base_dept
 */
@TableName(value = "base_dept")
@Data
public class BaseDept extends BaseEntity implements Serializable, ITreeNode<BaseDept> {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long deptId;

    /**
     * 上级部门ID
     */
    private Long deptPid;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 部门编码
     */
    private String deptCode;

    /**
     * 部门全称
     */
    private String deptFullName;

    /**
     * 启用
     */
    private YesNo enabled;

    /**
     * 顺序号
     */
    private String orderNo;

    @TableField(exist = false)
    private List<BaseDept> children;
    /**
     * 组织机构ID
     */
    private Long orgId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public Object id() {
        return this.deptId;
    }

    @Override
    public Object pid() {
        return this.deptPid;
    }
}