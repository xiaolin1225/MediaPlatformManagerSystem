/*
 * Copyright © 2023 - 现在. LMY. All Rights Reserved.
 */

package com.xiaolin.mpms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class PoolConfig {
    @Bean//return new AsyncResult<>(res);
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.initialize();  // 设置核心线程数
        executor.setCorePoolSize(4);  // 设置最大线程数
        executor.setMaxPoolSize(32); // 设置队列容量
        executor.setQueueCapacity(512); // 设置线程活跃时间（秒）
        executor.setKeepAliveSeconds(60); // 设置默认线程名称
        executor.setThreadNamePrefix("ThreadPool-"); // 设置拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        return executor;
    }
}