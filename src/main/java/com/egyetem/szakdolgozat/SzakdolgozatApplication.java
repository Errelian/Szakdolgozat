package com.egyetem.szakdolgozat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@SpringBootApplication
@EnableAsync
public class SzakdolgozatApplication {

    @Bean
    public TaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setThreadNamePrefix("Async-");
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors() - 1);
        executor.setMaxPoolSize(Runtime.getRuntime().availableProcessors() - 1);
        executor.setQueueCapacity(100);
        executor.afterPropertiesSet();
        return executor;
    }

    public static void main(String[] args) {
        SpringApplication.run(SzakdolgozatApplication.class, args);
    }

}
