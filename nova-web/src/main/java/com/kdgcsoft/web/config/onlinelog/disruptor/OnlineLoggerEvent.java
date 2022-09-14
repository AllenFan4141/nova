package com.kdgcsoft.web.config.onlinelog.disruptor;


import com.kdgcsoft.web.config.onlinelog.LoggerMessage;

/**
 * @description: 进程日志事件内容载体
 * @author: fyin
 * @date: 2018/10/22/022 15:26
 * @version: 1.0
 **/
public class OnlineLoggerEvent {

    private LoggerMessage log;

    public LoggerMessage getLog() {
        return log;
    }

    public void setLog(LoggerMessage log) {
        this.log = log;
    }
}
