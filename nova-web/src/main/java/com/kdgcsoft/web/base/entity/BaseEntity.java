package com.kdgcsoft.web.base.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author fyin
 * @date 2021-04-25 14:24
 */
@Getter
@Setter
public class BaseEntity {

    /**
     * 创建人默认属性名
     */
    public static final String PROPERTY_CREATE_BY = "createBy";
    /**
     * 修改人默认属性名
     */
    public static final String PROPERTY_UPDATE_BY = "updateBy";
    /**
     * 创建时间默认属性名
     */
    public static final String PROPERTY_CREATE_TIME = "createTime";
    /**
     * 更新时间默认属性名
     */
    public static final String PROPERTY_UPDATE_TIME = "updateTime";


    @JsonIgnore
    @JSONField(serialize = false)
    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty(hidden = true)
    private Integer deleted = 0;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    private Long createBy;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(hidden = true)
    private Long modifyBy;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date createTime;

    @JsonIgnore
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(hidden = true)
    @JSONField(serialize = false)
    private Date modifyTime;
}
