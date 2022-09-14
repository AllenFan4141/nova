package com.kdgcsoft.common.anno;

import java.lang.annotation.*;

/**
 * 字典转换的注解,会将当前属性的值转换为对应的文字写入到指定的字段中
 *
 * @author fyin
 * @date 2021-06-18 15:10
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DicBind {
    /**
     * 字典编码
     *
     * @return
     */
    String code() default "";

    /**
     * 将字典文字写入到哪个属性中
     *
     * @return
     */
    String target();
}
