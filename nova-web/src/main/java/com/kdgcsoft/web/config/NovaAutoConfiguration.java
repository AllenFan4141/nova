package com.kdgcsoft.web.config;

import cn.hutool.extra.spring.EnableSpringUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.kdgcsoft.web.module.ModuleManager;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.core.Ordered;

import javax.sql.DataSource;

/**
 * @author fyin
 * @date 2022年08月30日 9:33
 */
@AutoConfiguration(after = {FlywayAutoConfiguration.class})
@EnableSpringUtil
@EnableConfigurationProperties(NovaProperties.class)
@EnableCaching
public class NovaAutoConfiguration {
    @Autowired
    NovaProperties novaProperties;

    /**
     * 初始化ModuleManager, 保证要在flyway之后执行,以避免初始化的时候ddl还没执行就开始读取数据了
     *
     * @param flywayMigrationInitializer
     * @param configurableApplicationContext
     * @return
     */
    @Bean(initMethod = "init")
    public ModuleManager moduleManager(FlywayMigrationInitializer flywayMigrationInitializer, ConfigurableApplicationContext configurableApplicationContext) {
        return new ModuleManager(configurableApplicationContext, novaProperties);
    }
}
