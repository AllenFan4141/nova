package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.entity.BaseUser;
import com.kdgcsoft.web.base.service.BaseUserService;
import com.kdgcsoft.web.common.model.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

/**
 * @author fyin
 * @date 2022年09月01日 11:36
 */
@RestController
@Validated
@RequestMapping("/baseUser")
public class BaseUserController extends BaseController {

    @Autowired
    BaseUserService baseService;

    @PostMapping("/save")
    public JsonResult<BaseUser> save(@Validated BaseUser entity) {
        //检查重复性
        if (baseService.hasRepeat(entity)) {
            return JsonResult.ERROR("用户登录名重复");
        } else {
            baseService.saveOrUpdate(entity);
            return JsonResult.OK("保存成功").data(entity);
        }
    }


    @GetMapping("/getById")
    public JsonResult<BaseUser> getById(@NotNull Long id) {
        return JsonResult.OK().data(baseService.getById(id));
    }


    @GetMapping("/deleteById")
    public JsonResult deleteById(@NotNull Long id) {
        return JsonResult.OK().data(baseService.removeById(id));
    }

}
