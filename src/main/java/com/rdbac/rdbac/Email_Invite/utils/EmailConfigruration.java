package com.rdbac.rdbac.Email_Invite.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.rdbac.rdbac.Organisation.Controller.e;

@Configuration
@EnableAsync
public class EmailConfigruration {

    @Bean("emailExecutor")
    public Executor emailExecutor() {
       ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
       executor.setCorePoolSize(10);
       executor.setMaxPoolSize(15);
       executor.setQueueCapacity(100);
       executor.setThreadNamePrefix("EmailSender-");
       executor.initialize();
       return executor;
    }

}
