package com.kdgcsoft.web.config.security.handler;

import com.kdgcsoft.web.base.service.TokenService;
import com.kdgcsoft.web.common.consts.I18N;
import com.kdgcsoft.web.common.consts.WebConst;
import com.kdgcsoft.web.common.model.JsonResult;
import com.kdgcsoft.web.common.model.LoginUser;
import com.kdgcsoft.web.common.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 普通非前后端登录方式的认证处理类
 *
 * @author fyin
 * @date 2022年09月06日 17:27
 */
@Component
@Slf4j
public class FormAuthenticationHandler implements AuthenticationSuccessHandler, AuthenticationFailureHandler, LogoutSuccessHandler {
    @Autowired
    TokenService tokenService;

    /**
     * 认证成功调用的方法,适用于非前后端分离项目,前后端分离项目一般是在拦截器中拦截jwt字符串来进行登录设置的
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ServletUtils.renderJson(response, JsonResult.OK(I18N.AUTH_SUCCESS));
    }

    /**
     * 认证失败调用的方法,适用于非前后端分离项目调用,前后端分离项目一般使用AuthenticationEntryPoint来进行登录异常的处理
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     *                  request.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        JsonResult jsonResult = JsonResult.ERROR("用户名密码出错");
        //密码输错提示
        if (exception instanceof BadCredentialsException) {
            //TODO 实现密码输错次数过多后锁定账户的功能
//            String username = request.getParameter("username");
//            int leftCount = baseUserService.addBadPasswordCount(username);
//            if (leftCount == 0) {
//                jsonResult.setMsg("账号密码错误次数达到限制,账号已锁定");
//            } else {
//                if (leftCount > 0 && leftCount < 4) {
//                    jsonResult.setMsg("用户密码出错,您还有次" + leftCount + "尝试机会");
//                }
//            }
        } else if (exception instanceof LockedException) {
            jsonResult.setMsg("账号已锁定,请联系管理员");
        } else if (exception instanceof AccountExpiredException) {
            jsonResult.setMsg("账号因长时间未登录已经休眠,请联系管理员");
        }

        ServletUtils.renderJson(response, jsonResult);
    }

    /**
     * (非)前后端分离项目通用的处理方法,用来处理用户退出登录后相关的操作
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        ServletUtils.renderJson(response, JsonResult.OK(I18N.AUTH_LOGOUT));
    }
}
