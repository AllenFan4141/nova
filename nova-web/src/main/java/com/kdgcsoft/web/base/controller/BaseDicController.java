package com.kdgcsoft.web.base.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.web.base.entity.BaseDic;
import com.kdgcsoft.web.base.entity.BaseDicItem;
import com.kdgcsoft.web.base.service.BaseDicItemService;
import com.kdgcsoft.web.base.service.BaseDicService;
import com.kdgcsoft.web.base.service.BaseMenuService;
import com.kdgcsoft.web.common.model.JsonResult;
import com.kdgcsoft.web.common.model.PageRequest;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author fyin
 * @date 2022年09月01日 11:36
 */
@RestController
@Validated
@RequestMapping("/baseDic")
public class BaseDicController extends BaseController {

    @Autowired
    BaseDicService baseDicService;
    @Autowired
    BaseDicItemService baseDicItemService;

    @GetMapping(value = Api.PAGE)
    public JsonResult<List<BaseDic>> pageDic(String search, PageRequest pageRequest) {
        baseDicService.pageDic(pageRequest, search);
        return JsonResult.OK().data(pageRequest);
    }

    @GetMapping(value = "/listDicItem")
    public JsonResult<List<BaseDicItem>> listDicItemByCode(@ApiParam(value = "字典编码", required = true)
                                                           @NotBlank(message = "字典编码不能为空")
                                                           String code) {
        if (StrUtil.isEmpty(code)) {
            return JsonResult.OK().data(CollUtil.empty(BaseDicItem.class));
        }
        return JsonResult.OK().data(baseDicItemService.listItemsByDicCode(code));
    }

    @GetMapping(Api.GET_BY_CODE)
    public JsonResult<BaseDic> getDicByCode(@ApiParam(value = "字典编码", required = true)
                                            @NotBlank(message = "字典编码不能为空") String code) {
        return JsonResult.OK().data(baseDicService.getDicByCode(code));
    }

    @PostMapping(Api.SAVE)
    public JsonResult<BaseDic> saveDic(@RequestBody @Validated BaseDic entity) {
        return JsonResult.OK().data(baseDicService.saveDic(entity));
    }

    @GetMapping(Api.DEL_BY_CODE)
    public JsonResult delDicByCode(@ApiParam(value = "字典编码", required = true)
                                   @NotBlank(message = "字典编码不能为空") String code) {
        baseDicService.deleteDicByCode(code);
        return JsonResult.OK();

    }


}
