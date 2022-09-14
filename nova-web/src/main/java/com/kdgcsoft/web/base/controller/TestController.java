package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.service.BaseAuthService;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.common.model.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

/**
 * @author fyin
 * @date 2022年09月01日 11:36
 */
@Api(value = "测试接口", tags = "测试接口")
@RestController
@RequestMapping()
public class TestController extends BaseController {


    @ApiOperation("测试")
    @PostMapping("test")
    public JsonResult test() {
        return JsonResult.OK();
    }

}
