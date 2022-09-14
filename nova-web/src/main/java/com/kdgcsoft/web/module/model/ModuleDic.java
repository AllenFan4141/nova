package com.kdgcsoft.web.module.model;

import cn.hutool.core.lang.Pair;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fyin
 * @date 2020-04-03 9:13
 */
@Setter
@Getter
@Accessors(chain = true)
public class ModuleDic {
    private String code;
    private String name;
    private String description;
    private List<Pair<String,String>> options = new ArrayList<>();

    public void addOption(String text, String value) {
        this.options.add(Pair.of(text, value));
    }
}
