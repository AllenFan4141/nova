package com.kdgcsoft.web.base.enums;

import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author fyin
 * @date 2021-05-11 14:47
 */
@Dic("用户状态")
@ApiModel("用户状态")
public enum UserStatus implements IDic {
    /**
     * 正常
     */
    @ApiModelProperty("正常")
    NORMAL("正常"),
    @ApiModelProperty("停用")
    DISABLE("停用"),
    @ApiModelProperty("锁定")
    LOCK("锁定"),
    @ApiModelProperty("休眠,超过指定时间未登录,则休眠")
    DORMANT("休眠"),
    @ApiModelProperty("密码到期")
    EXPIRE("密码到期");
    private String text;

    UserStatus(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
