package com.kdgcsoft.web.config.mvc.resolver;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.kdgcsoft.web.common.model.PageRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 对前台传来的分页参数进行自动注入操作
 * 自动注入controller参数中的PageRequest
 * public JsonResult page(PageRequest page){
 *
 * }
 * @author fyin
 * @date 2021-04-27 18:02
 */
public class PageRequestHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    /**
     * 当前页参数名
     */
    public static final String CURRENT = "page";
    /**
     * 每页条数参数名
     */
    public static final String SIZE = "rows";
    /**
     * 排序参数名
     */
    public static final String ORDERS = "orders";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(PageRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) {
        long current = NumberUtil.parseLong(nativeWebRequest.getParameter(CURRENT));
        long size = NumberUtil.parseLong(nativeWebRequest.getParameter(SIZE));
        String orders=nativeWebRequest.getParameter(ORDERS);
        PageRequest page = new PageRequest();
        if(current>0){
            page.setCurrent(current);
        }
        if(size>0){
            page.setSize(size);
        }
        if(StrUtil.isNotEmpty(orders)){
            page.setOrders(orders);
        }
        return page;
    }
}
