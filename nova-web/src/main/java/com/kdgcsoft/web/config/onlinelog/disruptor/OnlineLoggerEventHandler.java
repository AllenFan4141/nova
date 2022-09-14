package com.kdgcsoft.web.config.onlinelog.disruptor;


import com.lmax.disruptor.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @description: 进程日志事件处理器
 * @author: fyin
 * @date: 2018/10/22/022 15:26
 * @version: 1.0
 **/
@Component
public class OnlineLoggerEventHandler implements EventHandler<OnlineLoggerEvent> {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void onEvent(OnlineLoggerEvent stringEvent, long l, boolean b) {
        messagingTemplate.convertAndSend("/logger/onlineLogger", stringEvent.getLog());
    }
}
