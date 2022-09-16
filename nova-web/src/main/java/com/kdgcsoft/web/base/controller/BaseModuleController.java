package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.service.BaseMenuService;
import com.kdgcsoft.web.module.ModuleManager;
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
@RequestMapping("/baseModule")
public class BaseModuleController extends BaseController {

    @Autowired
    ModuleManager moduleManager;

}
