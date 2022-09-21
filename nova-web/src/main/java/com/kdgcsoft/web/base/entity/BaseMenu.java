package com.kdgcsoft.web.base.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.common.interfaces.ITreeNode;
import com.kdgcsoft.web.base.enums.MenuOpenType;
import com.kdgcsoft.web.base.enums.YesNo;
import com.kdgcsoft.web.common.consts.WebConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @TableName base_menu
 */
@ApiModel("菜单")
@TableName(value = "base_menu")
@Setter
@Getter
@Accessors(chain = true)
public class BaseMenu extends BaseEntity implements Serializable, ITreeNode<BaseMenu> {

    @ApiModelProperty("主键")
    @TableId(type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("上级菜单编码")
    private String pcode = WebConst.DEF_TREE_ROOT_ID.toString();

    @ApiModelProperty("菜单编码")
    @NotBlank(message = "菜单编码不能为空")
    private String code;

    @ApiModelProperty(value = "名称", notes = "菜单的标题文字")
    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @ApiModelProperty("地址")
    private String url;

    @ApiModelProperty(value = "图标", notes = "没有设置时会使用默认图标")
    private String icon;

    @ApiModelProperty(value = "是否内置", notes = "内置菜单由程序自动加载,不可删除,可修改部分属性")
    private YesNo embed = YesNo.N;

    @ApiModelProperty("打开方式")
    private MenuOpenType openType = MenuOpenType.TAB;

    @ApiModelProperty("排序")
    private Integer orderNo;


    @ApiModelProperty("是否启用")
    private YesNo enabled = YesNo.N;

    @TableField(exist = false)
    private List<BaseMenu> children;

    @Override
    public Object id() {
        return this.code;
    }

    @Override
    public Object pid() {
        return this.pcode;
    }
}