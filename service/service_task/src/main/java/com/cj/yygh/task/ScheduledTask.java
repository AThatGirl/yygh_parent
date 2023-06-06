package com.cj.yygh.task;

import com.cj.yygh.rabbit.MqConst;
import com.cj.yygh.rabbit.RabbitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ScheduledTask
 * description:
 * 2023/6/6 11:36
 * Create by 杰瑞
 */

@Component
public class ScheduledTask {

    @Autowired
    private RabbitService rabbitService;


    @Scheduled(cron = "*/30 * * * * ?")
    public void printTime() {
        rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_TASK , MqConst.ROUTING_TASK_8, "");
    }

}
