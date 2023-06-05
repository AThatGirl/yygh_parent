package com.cj.yygh.hosp.listener;

import com.cj.yygh.hosp.service.ScheduleService;
import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.rabbit.MqConst;
import com.cj.yygh.rabbit.RabbitService;
import com.cj.yygh.vo.msm.MsmVo;
import com.cj.yygh.vo.order.OrderMqVo;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * HospitalReceiver
 * description:
 * 2023/5/31 17:01
 * Create by 杰瑞
 */

@Component
public class HospitalReceiver {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private RabbitService rabbitService;

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(name = MqConst.QUEUE_ORDER, durable = "true"),
                    exchange = @Exchange(name = MqConst.EXCHANGE_DIRECT_ORDER),
                    key = MqConst.ROUTING_ORDER)
    })
    public void consume(OrderMqVo orderMqVo, Message message, Channel channel) {

        scheduleService.update(orderMqVo);

        MsmVo msmVo = orderMqVo.getMsmVo();
        if(null != msmVo) {
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);
        }

    }


}
