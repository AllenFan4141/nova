package com.kdgcsoft.web.config.onlinelog;

import cn.hutool.core.thread.NamedThreadFactory;
import com.kdgcsoft.web.config.onlinelog.disruptor.OnlineLoggerEvent;
import com.kdgcsoft.web.config.onlinelog.disruptor.OnlineLoggerEventFactory;
import com.kdgcsoft.web.config.onlinelog.disruptor.OnlineLoggerEventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description:Disruptor 环形队列
 * @author: fyin
 * @date: 2018/10/22/022 15:25
 * @version: 1.0
 **/
@Component
public class LoggerDisruptorQueue {

    /**
     * The factory for the event
     */
    private OnlineLoggerEventFactory factory = new OnlineLoggerEventFactory();


    /**
     * Specify the size of the ring buffer, must be power of 2.
     **/
    private int bufferSize = 2 * 1024;
    /**
     * Construct the Disruptor
     **/
    private Disruptor<OnlineLoggerEvent> onlineLoggerEventDisruptor = new Disruptor<>(factory, bufferSize, new NamedThreadFactory("onlinelog", false));


    private static RingBuffer<OnlineLoggerEvent> onlineLoggerEventRingBuffer;


    @Autowired
    LoggerDisruptorQueue(OnlineLoggerEventHandler eventHandler) {
        onlineLoggerEventDisruptor.handleEventsWith(eventHandler);
        onlineLoggerEventRingBuffer = onlineLoggerEventDisruptor.getRingBuffer();
        onlineLoggerEventDisruptor.start();
    }

    public static void publishEvent(LoggerMessage log) {
        // Grab the next sequence
        if (onlineLoggerEventRingBuffer == null) {
            return;
        }
        long sequence = onlineLoggerEventRingBuffer.next();
        try {
            // Get the entry in the Disruptor
            OnlineLoggerEvent event = onlineLoggerEventRingBuffer.get(sequence);
            // for the sequence
            event.setLog(log);
            // Fill with data
        } finally {
            onlineLoggerEventRingBuffer.publish(sequence);
        }

    }
}
