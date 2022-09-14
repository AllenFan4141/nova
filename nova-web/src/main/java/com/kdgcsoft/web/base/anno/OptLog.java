package com.kdgcsoft.web.base.anno;

import com.kdgcsoft.web.base.enums.OptType;

import java.lang.annotation.*;

/**
 * 操作日志的注解信息
 *
 * @author fyin
 * @date 2022年09月13日 15:33
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OptLog {
    /**
     * 操作日志的标题
     *
     * @return
     */
    String title();

    /**
     * 操作类型
     *
     * @return
     */
    OptType type();

    /**
     * 可以使用模版的方式定义操作日志的记录文字内容
     *
     * @return
     */
    LogDetail detail() default @LogDetail(success = "", error = "");

    /**
     * 自定义指定操作人,默认为loginUser.getUserName
     *
     * @return
     */
    String operator() default "";

    /**
     * 是否记录请求参数
     *
     * @return
     */
    boolean logRequest() default true;

    /**
     * 是否记录响应结果
     *
     * @return
     */
    boolean logResponse() default true;

    /**
     * 操作成功记录的文字模版 支持SpEL表达式
     *
     * @return
     */
    String success() default "";

    /**
     * 操作失败的文字模版,支持SpEL表达式
     *
     * @return
     */
    String error() default "";

}
