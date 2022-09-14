package com.kdgcsoft.web.config.mvc;

import com.alibaba.fastjson2.support.spring.http.converter.FastJsonHttpMessageConverter;
import com.kdgcsoft.web.config.mvc.resolver.LoginUserHandlerMethodArgumentResolver;
import com.kdgcsoft.web.config.mvc.resolver.PageRequestHandlerMethodArgumentResolver;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fyin
 * @date 2021-04-27 12:34
 */
@Configuration
@EnableCaching
public class WebMvcConfiguration implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        /** 静态资源统一定位到static目录下访问路径 */
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/")
                .setUseLastModified(true).resourceChain(true);

        /** 配置knife4j 显示文档 */
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        /** 公共部分内容 */
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");

    }

    @Bean
    public LoginUserHandlerMethodArgumentResolver getLoginUserHandlerMethodArgumentResolver() {
        return new LoginUserHandlerMethodArgumentResolver();
    }

    @Bean
    public PageRequestHandlerMethodArgumentResolver pageHandlerMethodArgumentResolver() {
        return new PageRequestHandlerMethodArgumentResolver();
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(getLoginUserHandlerMethodArgumentResolver());
        argumentResolvers.add(pageHandlerMethodArgumentResolver());
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON);
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastMediaTypes);
//        converters.add(0, fastJsonHttpMessageConverter);
//    }
}
