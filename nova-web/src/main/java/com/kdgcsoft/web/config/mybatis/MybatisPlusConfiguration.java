package com.kdgcsoft.web.config.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.kdgcsoft.web.config.NovaProperties;
import com.kdgcsoft.web.config.mybatis.interceptor.PerformanceInterceptor;
import icu.mhb.mybatisplus.plugln.injector.JoinDefaultSqlInjector;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fyin
 * @date 2022年08月01日 10:44
 */
@Configuration
@MapperScan("com.kdgcsoft.**.mapper")
public class MybatisPlusConfiguration extends JoinDefaultSqlInjector {
    @Autowired
    NovaProperties novaProperties;

    /**
     * 自定义mybatisplus相关的默认属性
     *
     * @return
     */
    @Bean
    public MybatisPlusPropertiesCustomizer plusPropertiesCustomizer() {
        return plusProperties -> {
            /**关闭mybatis-plus 的banner打印**/
            plusProperties.getGlobalConfig().setBanner(false);
        };
    }

    /**
     * 性能日志记录插件
     *
     * @return
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setLogSql(novaProperties.isLogSql());
        performanceInterceptor.setPrintSql(novaProperties.isPrintSql());
        performanceInterceptor.setSlowSqlMillis(novaProperties.getSlowSqlMillis());
        performanceInterceptor.setLogOnlySlowSql(novaProperties.isLogOnlySlowSql());
        return performanceInterceptor;
    }

    /**
     * 添加mybatis插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        /**加入分页插件*/
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }
}
