package com.me10zyl.serverstatus.cron;

import com.me10zyl.serverstatus.util.ServerUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AutoRestartJob {

    @Scheduled(cron = "0 0 5 * * ?")
    public void restart(){
        ServerUtil.restartServer();
    }
}
