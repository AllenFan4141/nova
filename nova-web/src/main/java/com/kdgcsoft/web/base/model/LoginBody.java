package com.kdgcsoft.web.base.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author fyin
 * @date 2022年09月20日 11:00
 */
@Setter
@Getter
public class LoginBody {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    private boolean rememberMe;
}
