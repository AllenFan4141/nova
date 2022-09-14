package com.kdgcsoft.web.module.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统内置的功能菜单
 *
 * @author fyin
 * @date 2021-04-16 15:35
 */
@Setter
@Getter
public class ModuleMenu implements Serializable {
    private String code;
    private String name;
    private String url;
    private List<ModuleMenu> children = new ArrayList<>();
}
