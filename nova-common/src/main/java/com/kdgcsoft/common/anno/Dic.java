package com.kdgcsoft.common.anno;

import java.lang.annotation.*;

/**
 * 框架的字典注解,使用该注解标注的枚举类会加载到系统内置字典中
 *
 * @author fyin
 * @date 2021-05-11 14:28
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dic {
    /**
     * 字典编码,用来确定字典唯一性,不指定则使用短类名作为编码
     *
     * @return
     */
    String code() default "";

    /**
     * 字典中文名称
     *
     * @return
     */
    String value();

    /**
     * 字典描述
     * @return
     */
    String description() default "";
}
