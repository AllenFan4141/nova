package com.kdgcsoft.web.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import static com.kdgcsoft.web.common.consts.WebConst.ASYNC_EXECUTOR_NAME;

/**
 * spring 异步任务的配置信息,主要配置异步线程池,可以配置多个异步线程池
 * 在方法上可以使用@Async来执行异步任务
 *
 * @author fyin
 * @Async会默认从线程池获取线程，当然也可以显式的指定@Async("asyncTaskExecutor")
 * @date 2021-04-29 10:56
 */
@Configuration
@EnableAsync
public class AsyncConfiguration {
    /**
     * 线程池中允许的最大线程数，
     * 线程池中的当前线程数目不会超过该值。
     * 如果队列中任务已满，并且当前线程个数小于maximumPoolSize，那么会创建新的线程来执行任务。
     * 这里值得一提的是largestPoolSize，该变量记录了线程池在整个生命周期中曾经出现的最大线程个数。
     * 为什么说是曾经呢？因为线程池创建之后，可以调用setMaximumPoolSize()改变运行的最大线程的数目。
     */
    private static final int MAX_POOL_SIZE = 50;
    /**
     * 线程池的基本大小，即在没有任务需要执行的时候线程池的大小，并且只有在工作队列满了的情况下才会创建超出这个数量的线程.
     * 。这里需要注意的是：
     * 在刚刚创建ThreadPoolExecutor的时候，线程并不会立即启动，而是要等到有任务提交时才会启动，
     * 除非调用了prestartCoreThread/prestartAllCoreThreads事先启动核心线程。
     * 再考虑到keepAliveTime和allowCoreThreadTimeOut超时参数的影响，所以没有任务需要执行的时候，线程池的大小不一定是corePoolSize。
     */
    private static final int CORE_POOL_SIZE = 20;

    /**
     * 用于记录操作日志的异步线程池
     *
     * @return
     * @Async会默认从线程池获取线程，当然也可以显式的指定@Async("asyncExecutor")
     */
    @Bean(ASYNC_EXECUTOR_NAME)
    public AsyncTaskExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor asyncTaskExecutor = new ThreadPoolTaskExecutor();
        asyncTaskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
        asyncTaskExecutor.setCorePoolSize(CORE_POOL_SIZE);
        asyncTaskExecutor.setThreadNamePrefix("asyncPool-");
        asyncTaskExecutor.initialize();
        return asyncTaskExecutor;
    }
}
