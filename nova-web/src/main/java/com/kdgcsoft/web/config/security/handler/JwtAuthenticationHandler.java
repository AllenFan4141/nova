package com.kdgcsoft.web.config.security.handler;

import com.kdgcsoft.web.base.service.TokenService;
import com.kdgcsoft.web.common.consts.I18N;
import com.kdgcsoft.web.common.model.JsonResult;
import com.kdgcsoft.web.common.model.LoginUser;
import com.kdgcsoft.web.common.util.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 专门针对JWT前后端分离方式进行认证处理的处理类
 *
 * @author fyin
 * @date 2022年09月07日 15:24
 */
@Component
public class JwtAuthenticationHandler implements AuthenticationEntryPoint, LogoutSuccessHandler {
    @Autowired
    TokenService tokenService;
    @Autowired
    ApplicationEventPublisher applicationEventPublisher;

    /**
     * 认证失败时的处理逻辑
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        JsonResult result = JsonResult.ERROR(I18N.AUTH_UNAUTHORIZED);
        ServletUtils.renderJson(response, result);
    }

    /**
     * jwt方式退出登录时的处理逻辑,因为jwt的方式退出登录是无会话的,且jwt的拦截器一般都放行退出登录的地址,所以拦截器并不会进行拦截器的认证,所以进入该方法的时候
     * Authentication authentication是null 所以spring内部的事件发布机制并不会发布LogoutSuccess的事件
     * 所以需要手动发布事件来达到统计记录用户登录信息的操作
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //TODO 发送退出登录的事件 用于系统记录
        LoginUser loginUser = tokenService.getLoginUserFromRequest(request);
        if (loginUser != null) {
            // 删除用户缓存记录
            tokenService.delLoginUser(loginUser.getUuid());
        }
        ServletUtils.renderJson(response, JsonResult.OK(I18N.AUTH_LOGOUT));
    }
}
