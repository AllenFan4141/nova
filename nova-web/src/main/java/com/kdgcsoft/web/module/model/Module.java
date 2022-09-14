package com.kdgcsoft.web.module.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fyin
 * @date 2021-05-12 10:34
 */
@Setter
@Getter
@Accessors(chain = true)
public class Module implements Serializable {
    private String name;
    private String code;
    private int order = Integer.MAX_VALUE;
    private String description;
    private List<ModuleParam> params = new ArrayList<>();
    private List<ModuleMenu> menus = new ArrayList<>();
    private List<String> whitelist = new ArrayList<>();
    private URL baseUrl;

    public Module(String name, String code, int order) {
        this.name = name;
        this.code = code;
        this.order = order;
    }

    public Module() {
    }
}
