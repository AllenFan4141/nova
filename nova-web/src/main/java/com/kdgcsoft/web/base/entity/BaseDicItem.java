package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.common.interfaces.ITreeNode;
import com.kdgcsoft.web.base.enums.YesNo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @TableName base_dic_item
 */
@ApiModel("字典项")
@TableName(value = "base_dic_item")
@Setter
@Getter
@Accessors(chain = true)
public class BaseDicItem extends BaseEntity implements ITreeNode<BaseDicItem>, Serializable {
    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("字典编码")
    private String dicCode;

    @ApiModelProperty(value = "上级字典项ID", notes = "树形字典用来维护上下级关系的字段,默认为0")
    private Long pid;

    @ApiModelProperty(value = "是否内置", notes = "内置的字典项由程序自动加载,不可删除")
    private YesNo embed;

    @ApiModelProperty("值")
    private String value;

    @ApiModelProperty("文字")
    private String text;

    @ApiModelProperty("排序")
    private Integer orderNo = 0;
    @ApiModelProperty("备注")
    private String memo;

    @Override
    public Object id() {
        return this.value;
    }

    @Override
    public Object pid() {
        return this.pid;
    }

    @TableField(exist = false)
    private List<BaseDicItem> children;


    public String getText() {
        return this.text;
    }

    private transient String ptext;
}