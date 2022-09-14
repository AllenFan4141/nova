package com.kdgcsoft.web.module.event;

import com.kdgcsoft.web.module.model.Module;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author fyin
 * @date 2021-05-14 17:33
 */
public class ModuleManagerInitedEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public ModuleManagerInitedEvent(Object source) {
        super(source);
    }

}
