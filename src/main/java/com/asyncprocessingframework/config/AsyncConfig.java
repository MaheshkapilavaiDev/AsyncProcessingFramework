package com.asyncprocessingframework.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean("bankExecutor")
    public Executor taskExecutor() {

        ThreadPoolTaskExecutor executor =
                new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);

        executor.setMaxPoolSize(50);

        executor.setQueueCapacity(500);

        executor.setThreadNamePrefix("BANK-");

        executor.initialize();

        return executor;
    }
}
