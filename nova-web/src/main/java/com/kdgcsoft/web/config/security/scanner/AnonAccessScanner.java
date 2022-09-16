package com.kdgcsoft.web.config.security.scanner;

import cn.hutool.core.util.ReUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * 匿名访问扫描器
 * {@link AnonAccess} 标记的controller方法允许匿名访问的url
 *
 * @author fyin
 * @date 2022年08月18日 14:47
 */
@Component
public class AnonAccessScanner implements InitializingBean, ApplicationContextAware {
    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");
    public static final String ASTERISK = "*";

    private List<String> anonymousUrls = new ArrayList<>();

    private ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);

            // 获取方法上边的注解 替代path variable 为 *
            AnonAccess method = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), AnonAccess.class);
            Optional.ofNullable(method).ifPresent(anonymous -> info.getPatternsCondition().getPatterns()
                    .forEach(url -> anonymousUrls.add(ReUtil.replaceAll(url, PATTERN, ASTERISK))));

            // 获取类上边的注解, 替代path variable 为 *
            AnonAccess controller = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), AnonAccess.class);
            Optional.ofNullable(controller).ifPresent(anonymous -> info.getPatternsCondition().getPatterns()
                    .forEach(url -> anonymousUrls.add(ReUtil.replaceAll(url, PATTERN, ASTERISK))));
        });
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<String> getAnonymousUrls() {
        return anonymousUrls;
    }
}
