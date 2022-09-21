package com.kdgcsoft.web.base.controller;

import cn.hutool.core.bean.BeanUtil;
import com.kdgcsoft.web.base.service.BaseMenuService;
import com.kdgcsoft.web.base.service.BaseRoleAuthService;
import com.kdgcsoft.web.base.vo.LoginVo;
import com.kdgcsoft.web.base.service.BaseAuthService;
import com.kdgcsoft.web.base.vo.UserInfoVo;
import com.kdgcsoft.web.common.consts.I18N;
import com.kdgcsoft.web.common.model.JsonResult;
import com.kdgcsoft.web.common.model.LoginUser;
import com.kdgcsoft.web.config.security.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    BaseRoleAuthService baseRoleAuthService;
    @Autowired
    BaseMenuService baseMenuService;

    @ApiOperation("登陆")
    @PostMapping(Api.LOGIN)
    public JsonResult login(@RequestBody @Valid LoginVo loginVo) {
        return baseAuthService.login(loginVo.getUsername(), loginVo.getPassword(), loginVo.isRememberMe());
    }


    @GetMapping("/getUserInfo")
    public JsonResult getUserInfo() {
        LoginUser loginUser = SecurityUtil.getLoginUser();
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtil.copyProperties(loginUser, userInfoVo);
        if (loginUser.isRoot()) {
            userInfoVo.setPermissions(baseMenuService.getAllMenuCodes());
        } else {
            userInfoVo.setPermissions(baseRoleAuthService.getUserAuthCodes(loginUser));
        }
        return JsonResult.OK().data(userInfoVo);
    }

    @GetMapping("/getMenus")
    public JsonResult getMenus(LoginUser loginUser) {
        if (loginUser.isRoot()) {
            return JsonResult.OK().data(baseMenuService.tree());
        } else {
            return JsonResult.OK().data(baseMenuService.userMenuTree(loginUser));
        }
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
