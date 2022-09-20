package com.kdgcsoft.document.config;

import com.kdgcsoft.document.DocumentManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author fyin
 * @date 2022年09月20日 11:42
 */
@EnableAutoConfiguration
@EnableConfigurationProperties(DocumentProperties.class)
public class DocumentConfiguration {
    @Autowired
    DocumentProperties documentProperties;

    @Bean(initMethod = "init")
    public DocumentManager documentManager(ApplicationContext applicationContext) {
        return new DocumentManager(documentProperties, applicationContext);
    }
}
