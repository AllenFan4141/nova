package com.kdgcsoft.web.base.entity;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.kdgcsoft.common.interfaces.ITreeNode;
import com.kdgcsoft.web.base.enums.Embed;
import com.kdgcsoft.web.base.enums.Enabled;
import com.kdgcsoft.web.base.enums.MenuOpenType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
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
    @TableId
    private Long id;

    @ApiModelProperty("上级菜单编码")
    private String pcode;

    @ApiModelProperty("菜单编码")
    private String code;

    @ApiModelProperty(value = "名称", notes = "菜单的标题文字")
    private String name;

    @ApiModelProperty("地址")
    private String url;

    @ApiModelProperty(value = "图标", notes = "没有设置时会使用默认图标")
    private String icon;

    @ApiModelProperty(value = "是否内置", notes = "内置菜单由程序自动加载,不可删除,可修改部分属性")
    private Embed embed = Embed.N;

    @ApiModelProperty("打开方式")
    private MenuOpenType openType;

    @ApiModelProperty("排序")
    private Integer orderNo;


    @ApiModelProperty("是否启用")
    private Enabled enabled = Enabled.N;

    private transient List<ITreeNode> children;

    private transient String text;
    private transient String iconCls;

    @Override
    public Object id() {
        return this.code;
    }

    @Override
    public Object pid() {
        return this.pcode;
    }

    @Override
    public void addChild(BaseMenu node) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(node);
    }

    public String getText() {
        return this.name;
    }


    public String getIconCls() {
        return StrUtil.isNotEmpty(this.icon) ? "icon " + this.icon : "icon icon-window";
    }
}