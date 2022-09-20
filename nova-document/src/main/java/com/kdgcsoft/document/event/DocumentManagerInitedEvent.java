package com.kdgcsoft.document.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author fyin
 * @date 2021-06-29 16:09
 */
public class DocumentManagerInitedEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public DocumentManagerInitedEvent(Object source) {
        super(source);
    }
}
