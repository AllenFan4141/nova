package com.kdgcsoft.web.config.onlinelog;

import lombok.Getter;
import lombok.Setter;

/**
 * @description: 日志消息实体
 * @author: fyin
 * @date: 2018/10/22/022 15:26
 * @version: 1.0
 **/
@Setter
@Getter
public class LoggerMessage {

    private String body;
    private String timestamp;
    private String threadName;
    private String className;
    private String level;
    private String line;

    public LoggerMessage(String body, String timestamp, String threadName, String className, String level, String line) {
        this.body = body;
        this.timestamp = timestamp;
        this.threadName = threadName;
        this.className = className;
        this.level = level;
        this.line = line;
    }

    public LoggerMessage() {
    }
}
