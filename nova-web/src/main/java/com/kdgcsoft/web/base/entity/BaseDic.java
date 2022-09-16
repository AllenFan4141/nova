package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.common.anno.DicBind;
import com.kdgcsoft.web.base.enums.DicType;
import com.kdgcsoft.web.base.enums.YesNo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @TableName base_dic
 */
@ApiModel("字典")
@TableName(value = "base_dic")
@Setter
@Getter
@Accessors(chain = true)
public class BaseDic extends BaseEntity implements Serializable {

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("编码")
    @NotEmpty(message = "字典编码不能为空")
    private String code;

    @NotEmpty(message = "字典名称不能为空")
    @ApiModelProperty("名称")
    private String name;

    /**
     * 字典类型 LIST,TREE
     */
    @ApiModelProperty("字典类型")
    @DicBind(target = "dicType_text")
    private DicType dicType;

    private transient String dicType_text;

    @ApiModelProperty(value = "是否内置", notes = "内置字典是由程序自动加载的")
    private YesNo embed = YesNo.N;

    @ApiModelProperty("备注")
    private String memo;


    private transient List<BaseDicItem> items = new ArrayList<>();

    public void addItem(String text, String value, int order) {
        if (items == null) {
            items = new ArrayList<>();
        }
        items.add(new BaseDicItem().setText(text).setValue(value).setOrderNo(order));
    }
}