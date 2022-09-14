package com.kdgcsoft.web.base.anno;

import java.lang.annotation.*;

/**
 * 操作日志的明细信息
 *
 * @author fyin
 * @date 2022年09月13日 15:38
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogDetail {
    /**
     * 操作成功时记录的日志明细信息 可以使用SPEL表达式来占位
     *
     * @return
     */
    String success();

    /**
     * 操作失败时记录的日志明细信息 可以使用SPEL表达式来占位
     *
     * @return
     */
    String error();
}
