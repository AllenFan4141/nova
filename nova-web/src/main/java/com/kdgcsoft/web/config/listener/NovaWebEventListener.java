package com.kdgcsoft.web.config.listener;

import com.kdgcsoft.web.base.entity.BaseOptLog;
import com.kdgcsoft.web.base.event.OptLogEvent;
import com.kdgcsoft.web.base.service.BaseOptLogService;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.common.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 用于监听web中重要事件的事件监听器
 *
 * @author fyin
 * @date 2022年09月07日 10:28
 */
@Component
@Slf4j
public class NovaWebEventListener {
    @Autowired
    BaseOptLogService optLogService;

    /**
     * 监听认证成功进行记录
     */
    @Async(WebConst.ASYNC_EXECUTOR_NAME)
    @EventListener(AuthenticationSuccessEvent.class)
    public void loginSuccessEvent(AuthenticationSuccessEvent event) {
        LoginUser loginUser = (LoginUser) event.getAuthentication().getPrincipal();
        log.info(loginUser.getUsername() + "登录成功");
    }

    /**
     * 监听认证失败事件 用于记录
     *
     * @param event
     */
    @Async(WebConst.ASYNC_EXECUTOR_NAME)
    @EventListener(AbstractAuthenticationFailureEvent.class)
    public void loginErrorEvent(AbstractAuthenticationFailureEvent event) {

        log.info(event.getAuthentication().getName() + "登录出错:" + event.getException().getMessage());
    }

    /**
     * 监听用户退出登录事件
     *
     * @param event
     */
    @Async(WebConst.ASYNC_EXECUTOR_NAME)
    @EventListener(LogoutSuccessEvent.class)
    public void loginOutEvent(LogoutSuccessEvent event) {
        LoginUser loginUser = (LoginUser) event.getAuthentication().getPrincipal();
        log.info(loginUser.getUsername() + "退出登录");
    }

    /**
     * 监听用户操作日志事件,异步持久化
     */
    @Async(WebConst.ASYNC_EXECUTOR_NAME)
    @EventListener(OptLogEvent.class)
    public void optLogEvent(OptLogEvent optLogEvent) {
        BaseOptLog optLog = (BaseOptLog) optLogEvent.getSource();
        if (optLog != null) {
            optLogService.save(optLog);
        }
    }
}
