package com.kdgcsoft.web.module.model;

import cn.hutool.core.convert.BasicType;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.web.base.entity.BaseParam;
import com.kdgcsoft.web.base.enums.ParamType;
import com.kdgcsoft.web.base.enums.YesNo;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 模块可配置的参数信息
 *
 * @author fyin
 * @date 2021-04-16 15:23
 */
@Setter
@Getter
public class ModuleParam implements Serializable {
    private String code;
    private String name;
    private ParamType type;
    private String description;
    private String defaultValue;
    /**
     * 是否允许匿名访问
     */
    private boolean anonAccess = false;

    private List<Pair> options = new ArrayList<>();

    /**
     * 判断是否和数据库的数据一致
     *
     * @param db
     * @return
     */
    public boolean sameToDb(BaseParam db) {
        return this.code.equals(StrUtil.removePrefix(db.getCode(), db.getModuleCode() + StrUtil.DOT))
                && this.name.equals(db.getName())
                && this.type.equals(db.getParamType())
                && this.description.equals(db.getDescription())
                && db.getAnonAccess().equals(YesNo.ofBoolean(this.anonAccess))
                && this.defaultValue.equals(db.getDefaultValue());
    }

}
