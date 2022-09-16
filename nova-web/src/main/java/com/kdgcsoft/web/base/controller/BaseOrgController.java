package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.entity.BaseOrg;
import com.kdgcsoft.web.base.service.BaseOrgService;
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
@RequestMapping("/baseOrg")
public class BaseOrgController extends BaseController {

    @Autowired
    BaseOrgService baseService;

    @PostMapping("/save")
    public JsonResult<BaseOrg> save(@Validated BaseOrg entity) {
        //检查重复性
        if (baseService.hasRepeat(entity)) {
            return JsonResult.ERROR("组织机构编码重复");
        } else {
            baseService.saveOrUpdate(entity);
            return JsonResult.OK("保存成功").data(entity);
        }
    }


    @GetMapping("/getById")
    public JsonResult<BaseOrg> getById(@NotNull Long id) {
        return JsonResult.OK().data(baseService.getById(id));
    }

    @GetMapping("/tree")
    public JsonResult tree() {
        return JsonResult.OK().data(baseService.tree());
    }

    @GetMapping("/deleteById")
    public JsonResult deleteById(@NotNull Long id) {
        return JsonResult.OK().data(baseService.removeById(id));
    }

}
