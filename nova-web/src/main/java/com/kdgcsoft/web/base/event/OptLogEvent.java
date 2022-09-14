package com.kdgcsoft.web.base.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author fyin
 * @date 2021-07-05 9:17
 */
public class OptLogEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public OptLogEvent(Object source) {
        super(source);
    }
}
