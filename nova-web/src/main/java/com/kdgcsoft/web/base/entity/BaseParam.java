package com.kdgcsoft.web.base.entity;

import cn.hutool.core.convert.BasicType;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.common.util.NovaUtil;
import com.kdgcsoft.web.base.enums.Embed;
import com.kdgcsoft.web.base.enums.ParamType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author YINFAN
 * @TableName base_param
 */
@ApiModel("系统参数")
@TableName(value = "base_param")
@Setter
@Getter
public class BaseParam extends BaseEntity implements Serializable {
    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("模块编码")
    private String moduleCode;

    @ApiModelProperty("参数值")
    private String value;

    @ApiModelProperty("默认值")
    private String defaultValue;

    @ApiModelProperty("参数类型")
    private ParamType paramType;

    @ApiModelProperty("是否内置")
    private Embed embed = Embed.N;

    @ApiModelProperty("说明")
    private String description;

    @ApiModelProperty("格式化之后的参数值")
    @TableField(exist = false)
    private Object fmtValue;

    public Object getFmtValue() {
        Object fmtValue = this.value;
        switch (this.paramType) {
            case FILE:
            case IMAGE:
                //如果当前值跟默认值一样 则不进行值的转换
                if (StrUtil.equals(this.value, this.defaultValue)) {
                    fmtValue = this.value;
                } else {
                    if (NovaUtil.isUUID(this.value)) {
                        fmtValue = "api/base/file/download?id=" + this.value;
                    }
                }
                break;
            case NUMBER:
                fmtValue = Convert.toNumber(this.value);
                break;
            case BOOLEAN:
                fmtValue = Convert.toBool(this.value);
                break;
            default:
                fmtValue = this.value;
        }
        return fmtValue;
    }
}