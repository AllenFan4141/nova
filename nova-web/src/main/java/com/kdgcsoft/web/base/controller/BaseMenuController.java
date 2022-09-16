package com.kdgcsoft.web.base.controller;

import com.kdgcsoft.web.base.service.BaseMenuService;
import com.kdgcsoft.web.base.service.BaseOrgService;
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
@RequestMapping("/baseMenu")
public class BaseMenuController extends BaseController {

    @Autowired
    BaseMenuService baseMenuService;

}
