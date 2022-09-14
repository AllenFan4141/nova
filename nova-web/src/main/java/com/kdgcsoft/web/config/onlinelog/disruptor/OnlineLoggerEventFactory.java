package com.kdgcsoft.web.config.onlinelog.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @description: 进程日志事件工厂类
 * @author: fyin
 * @date: 2018/10/22/022 15:26
 * @version: 1.0
 **/
public class OnlineLoggerEventFactory implements EventFactory<OnlineLoggerEvent> {
    @Override
    public OnlineLoggerEvent newInstance() {
        return new OnlineLoggerEvent();
    }
}
