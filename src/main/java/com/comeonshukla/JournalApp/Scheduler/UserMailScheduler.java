package com.comeonshukla.JournalApp.Scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserMailScheduler {

    @Scheduled(cron = "0 * * ? * *")
    public void sendMailUsers(){
        System.out.println("Cron task executed at: " + System.currentTimeMillis());
    }
}
