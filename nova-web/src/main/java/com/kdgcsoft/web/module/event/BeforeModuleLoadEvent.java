package com.kdgcsoft.web.module.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author fyin
 * @date 2021-05-14 17:33
 */
public class BeforeModuleLoadEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public BeforeModuleLoadEvent(Object source) {
        super(source);
    }
}
