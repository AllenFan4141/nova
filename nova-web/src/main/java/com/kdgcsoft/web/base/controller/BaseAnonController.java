package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.common.model.JsonResult;
import com.kdgcsoft.web.module.ModuleManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fyin
 * @date 2022年09月15日 11:29
 */
@Api(value = "匿名访问的接口", tags = "匿名访问的接口")
@RestController
@RequestMapping("/anon")
public class BaseAnonController extends BaseController {
    @Autowired
    ModuleManager moduleManager;

    /**
     * 获取可匿名访问的参数键值对
     *
     * @param format 是否格式化值
     * @return
     */
    @ApiOperation("获取可匿名访问的参数信息")
    @PostMapping("/anonParams")
    public JsonResult annoParamsMap(@RequestParam(required = false, defaultValue = "true") boolean format) {
        if (format) {
            return JsonResult.OK().data(moduleManager.getAnonParamsFmtValueMap());
        } else {
            return JsonResult.OK().data(moduleManager.getAnonParamsValueMap());
        }

    }
}
