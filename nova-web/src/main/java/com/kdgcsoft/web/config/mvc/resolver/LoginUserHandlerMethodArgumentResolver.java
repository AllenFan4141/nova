package com.kdgcsoft.web.config.mvc.resolver;

import com.kdgcsoft.web.common.model.LoginUser;
import com.kdgcsoft.web.config.security.util.SecurityUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 自动注入controller参数中的LoginUser
 * public JsonResult index(LoginUser loginuser){
 * <p>
 * }
 *
 * @author fyin
 * @date 2021-04-27 18:02
 */
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        return SecurityUtil.getLoginUser();
    }
}
