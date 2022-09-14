package com.kdgcsoft.web.common.model;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.Setter;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 返回前端结果的统一格式
 *
 * @author fyin
 * @date 2022年08月29日 9:33
 */
@Setter
@Getter
public class JsonResult<T> implements Serializable {
    /**
     * 默认的成功消息
     */
    private static final String DEF_OK_MSG = "操作成功";
    /**
     * 默认的失败消息
     */
    private static final String DEF_ERROR_MSG = "操作失败";

    /**
     * 成功失败标识
     */
    private Boolean success = true;
    /**
     * 相应编码
     */
    private int code = HttpStatus.HTTP_OK;
    /**
     * 返回的消息信息
     */
    private String msg = DEF_OK_MSG;
    /**
     * 返回的数据
     */
    private T data;

    public JsonResult() {
    }

    public JsonResult(boolean success, String message) {
        this.success = success;
        this.msg = message;
    }

    public static JsonResult OK() {
        return OK(DEF_OK_MSG);
    }

    public static JsonResult OK(String message) {
        return new JsonResult(true, message);
    }

    public static JsonResult ERROR() {
        return ERROR(DEF_ERROR_MSG);
    }

    public static JsonResult ERROR(String message) {
        return new JsonResult(false, message);
    }

    public JsonResult data(T data) {
        this.data = data;
        return this;
    }

    public JsonResult msg(String msg) {
        this.msg = msg;
        return this;
    }

    public JsonResult code(int code) {
        this.code = code;
        return this;
    }

    /**
     * 如果JsonResult success为 true 则进行函数调用 返回函数返回值,方便代码链式编程
     *
     * @param supplier
     * @return
     */
    public JsonResult successThen(Supplier<JsonResult> supplier) {
        if (this.success) {
            return supplier.get();
        } else {
            return this;
        }
    }

    /**
     * 如果JsonResult success为 false 则进行函数调用 返回函数返回值,方便代码链式编程
     *
     * @param supplier
     * @return
     */
    public JsonResult errorThen(Supplier<JsonResult> supplier) {
        if (!this.success) {
            return supplier.get();
        } else {
            return this;
        }
    }

    /**
     * 转换为json字符串
     *
     * @return
     */
    public String toJsonString() {
        return JSON.toJSONString(this);
    }
}
