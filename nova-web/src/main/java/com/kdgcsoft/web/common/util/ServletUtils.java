package com.kdgcsoft.web.common.util;

import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.alibaba.fastjson2.JSON;
import com.kdgcsoft.web.common.model.JsonResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 客户端工具类
 *
 * @author fyin
 */
public class ServletUtils extends ServletUtil {
    /**
     * 用户UA请求头
     */
    public static final String USER_AGENT_HEADER = "User-Agent";

    /**
     * 获得请求User-Agent信息对象
     *
     * @return
     */
    public static UserAgent getUserAgent() {
        UserAgent userAgent = UserAgentUtil.parse(ServletUtil.getHeader(getRequest(), USER_AGENT_HEADER, StandardCharsets.UTF_8));
        return userAgent;
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取 HttpServletRequest
     */
    public static HttpServletResponse getResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        return response;
    }


    /**
     * 获得用户请求IP
     *
     * @return
     */
    public static String getClientIp() {
        return ServletUtil.getClientIP(getRequest());
    }

    /**
     * 返回json content-type为application/json
     *
     * @param response 渲染对象
     * @param json     待渲染的字符串
     */
    public static void renderJson(HttpServletResponse response, JsonResult json) {
        response.setCharacterEncoding("utf-8");
        ServletUtil.write(response, JSON.toJSONString(json), "application/json");
    }
}
