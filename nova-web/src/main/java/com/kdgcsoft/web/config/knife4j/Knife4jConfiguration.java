package com.kdgcsoft.web.config.knife4j;

import com.kdgcsoft.web.config.NovaProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author fyin
 * @date 2021-04-27 11:13
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Autowired
    NovaProperties novaProperties;

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title(novaProperties.getName() + "接口文档")
                        .description(novaProperties.getDescription())
                        .termsOfServiceUrl("http://www.kdgcsoft.com/")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName(novaProperties.getName() + "-api")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.kdgcsoft.**")
                        .and(RequestHandlerSelectors.withClassAnnotation(Api.class))
                        .and(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
