package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.anno.OptLog;
import com.kdgcsoft.web.base.enums.OptType;
import com.kdgcsoft.web.base.service.BaseAuthService;
import com.kdgcsoft.web.common.consts.I18N;
import com.kdgcsoft.web.common.model.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author fyin
 * @date 2022年09月01日 11:36
 */
@Api(value = "用户认证接口", tags = "用户认证接口")
@RestController
@Validated
@RequestMapping("/auth")
public class BaseAuthController extends BaseController {

    @Autowired
    BaseAuthService baseAuthService;

    @ApiOperation("登陆")
    @PostMapping("login")
    public JsonResult login(@ApiParam(value = "用户名", required = true) @NotBlank(message = "用户名不能为空") String username,
                            @ApiParam(value = "密码", required = true) @NotBlank(message = "密码不能为空") String password,
                            boolean rememberMe) {
        return baseAuthService.login(username, password, rememberMe);
    }

    /**
     * 这个requestMapping没有用 在springSecurity中配置地址后 spring会自动帮我们映射指定的地址
     *
     * @return
     */
    @ApiOperation("注销")
    @PostMapping("logout")
    public JsonResult logout() {
        return JsonResult.OK(I18N.AUTH_LOGOUT);
    }
}
