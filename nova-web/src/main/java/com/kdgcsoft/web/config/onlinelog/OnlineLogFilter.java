package com.kdgcsoft.web.config.onlinelog;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;

import java.sql.Date;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_MS_FORMAT;

/**
 * @description:
 * @author: fyin
 * @date: 2018/9/29/029 14:12
 * @version: 1.0
 **/
public class OnlineLogFilter extends Filter<ILoggingEvent> {

    @Override
    public FilterReply decide(ILoggingEvent event) {
        StackTraceElement[] cda = event.getCallerData();
        int line = 0;
        if (cda != null && cda.length > 0) {
            line = cda[0].getLineNumber();
        }
        LoggerMessage loggerMessage = new LoggerMessage(
                event.getFormattedMessage()
                , DateUtil.format(new Date(event.getTimeStamp()), NORM_DATETIME_MS_FORMAT),
                StrUtil.fillBefore(event.getThreadName(), ' ', 15),
                event.getLoggerName(),
                StrUtil.fillAfter(event.getLevel().levelStr, ' ', 5),
                StrUtil.fillAfter(String.valueOf(line), ' ', 4)
        );
        LoggerDisruptorQueue.publishEvent(loggerMessage);
        return FilterReply.NEUTRAL;
    }
}  