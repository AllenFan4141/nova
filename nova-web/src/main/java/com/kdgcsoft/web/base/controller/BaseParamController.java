package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.service.BaseParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fyin
 * @date 2022年09月01日 11:36
 */
@RestController
@Validated
@RequestMapping("/baseParam")
public class BaseParamController extends BaseController {

    @Autowired
    BaseParamService baseParamService;




}
