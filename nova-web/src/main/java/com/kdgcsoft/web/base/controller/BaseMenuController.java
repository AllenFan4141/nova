package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.entity.BaseDept;
import com.kdgcsoft.web.base.entity.BaseMenu;
import com.kdgcsoft.web.base.service.BaseMenuService;
import com.kdgcsoft.web.base.service.BaseOrgService;
import com.kdgcsoft.web.common.model.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @author fyin
 * @date 2022年09月01日 11:36
 */
@RestController
@Validated
@RequestMapping("/baseMenu")
public class BaseMenuController extends BaseController {

    @Autowired
    BaseMenuService baseMenuService;


    @GetMapping(Api.TREE)
    public JsonResult tree() {
        return JsonResult.OK().data(baseMenuService.tree());
    }

    @PostMapping(Api.SAVE)
    public JsonResult<BaseMenu> save(@Validated @RequestBody BaseMenu entity) {
        return JsonResult.OK().data(baseMenuService.saveBaseMenu(entity));
    }

    @GetMapping(Api.DEL_BY_ID)
    public JsonResult deleteById(@NotNull(message = "菜单ID不能为空") Long id) {
        baseMenuService.deleteById(id);
        return JsonResult.OK();
    }
}
