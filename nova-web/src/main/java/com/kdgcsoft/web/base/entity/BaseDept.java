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
    private Long deptPid = WebConst.DEF_TREE_ROOT_ID;
    /**
     * 组织机构ID
     */
    @NotNull(message = "组织机构不能为空")
    private Long orgId;
    /**
     * 部门名称
     */
    @NotBlank(message = "部门名称不能为空")
    private String deptName;

    /**
     * 部门编码
     */
    @NotBlank(message = "部门编码不能为空")
    private String deptCode;

    /**
     * 部门全称
     */
    private String deptFullName;

    /**
     * 启用
     */
    private YesNo enabled = YesNo.Y;

    /**
     * 顺序号
     */
    private Integer orderNo;

    @TableField(exist = false)
    private List<BaseDept> children;


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