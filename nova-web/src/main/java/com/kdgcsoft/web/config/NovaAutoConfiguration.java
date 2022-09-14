package com.kdgcsoft.web.config;

import cn.hutool.extra.spring.EnableSpringUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kdgcsoft.web.module.ModuleManager;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fyin
 * @date 2022年08月30日 9:33
 */
@AutoConfiguration(after = FlywayAutoConfiguration.class)
@EnableSpringUtil
@EnableConfigurationProperties(NovaProperties.class)
@EnableCaching
public class NovaAutoConfiguration {
    @Autowired
    NovaProperties novaProperties;

    @Bean(initMethod = "init")
    public ModuleManager moduleManager(ConfigurableApplicationContext configurableApplicationContext) {
        return new ModuleManager(configurableApplicationContext, novaProperties);
    }
}
