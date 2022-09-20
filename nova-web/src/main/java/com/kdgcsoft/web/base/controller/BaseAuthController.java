package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.vo.LoginVo;
import com.kdgcsoft.web.base.service.BaseAuthService;
import com.kdgcsoft.web.common.consts.I18N;
import com.kdgcsoft.web.common.model.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
    @PostMapping(Api.LOGIN)
    public JsonResult login(@RequestBody @Valid LoginVo loginVo) {
        return baseAuthService.login(loginVo.getUsername(), loginVo.getPassword(), loginVo.isRememberMe());
    }

    /**
     * 这个requestMapping没有用 在springSecurity中配置地址后 spring会自动帮我们映射指定的地址
     *
     * @return
     */
    @ApiOperation("注销")
    @PostMapping(Api.LOGOUT)
    public JsonResult logout() {
        return JsonResult.OK(I18N.AUTH_LOGOUT);
    }
}
