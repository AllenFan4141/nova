package com.kdgcsoft.web.base.enums;

import com.kdgcsoft.common.anno.Dic;
import com.kdgcsoft.common.interfaces.IDic;

/**
 * 操作类型
 *
 * @author fyin
 * @date 2022年09月13日 15:36
 */
@Dic("操作类型")
public enum OptType implements IDic {
    /**
     * 查询
     */
    SELECT("查询"),
    /**
     * 新增
     */
    INSERT("新增"),
    /**
     * 更新
     */
    UPDATE("更新"),
    /**
     * 删除
     */
    DELETE("删除"),
    /**
     * 登录
     */
    LOGIN("登录"),
    /**
     * 退出登录
     */
    LOGOUT("退出登录"),
    /**
     * 授权
     */
    GRANT("授权"),
    /**
     * 导出
     */
    EXPORT("导出"),
    /**
     * 导入
     */
    IMPORT("导入"),
    /**
     * 预览
     */
    PREVIEW("预览"),
    /**
     * 清除
     */
    CLEAN("清除"),
    /**
     * 测试
     */
    TEST("测试"),
    /**
     * 发布
     */
    PUBLISH("发布"),
    /**
     * 其他
     */
    OTHER("其他");

    private String text;

    OptType(String text) {
        this.text = text;
    }

    @Override
    public String text() {
        return this.text;
    }
}
