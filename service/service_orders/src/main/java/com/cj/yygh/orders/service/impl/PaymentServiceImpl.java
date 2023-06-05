package com.cj.yygh.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cj.yygh.enums.OrderStatusEnum;
import com.cj.yygh.enums.PaymentStatusEnum;
import com.cj.yygh.model.order.OrderInfo;
import com.cj.yygh.model.order.PaymentInfo;
import com.cj.yygh.orders.mapper.PaymentMapper;
import com.cj.yygh.orders.service.OrderInfoService;
import com.cj.yygh.orders.service.PaymentService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class PaymentServiceImpl extends ServiceImpl<PaymentMapper, PaymentInfo> implements PaymentService {


    @Autowired
    private OrderInfoService orderInfoService;


    @Override
    public void savePaymentInfo(OrderInfo orderInfo, Integer paymentType) {
        //如果该订单已经支付
        Integer count = baseMapper.selectCount(new QueryWrapper<PaymentInfo>().eq("order_id", orderInfo.getId()));
        if (count > 0) {
            return;
        }

        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(orderInfo.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setOutTradeNo(orderInfo.getOutTradeNo());
        paymentInfo.setPaymentStatus(PaymentStatusEnum.UNPAID.getStatus());
        String subject = new DateTime(orderInfo.getReserveDate()).toString("yyyy-MM-dd")+"|"+orderInfo.getHosname()+"|"+orderInfo.getDepname()+"|"+orderInfo.getTitle();
        paymentInfo.setSubject(subject);
        paymentInfo.setTotalAmount(orderInfo.getAmount());
        baseMapper.insert(paymentInfo);


    }

    @Override
    public void paySuccess(String outTradeNo, Integer status, Map<String, String> map) {

        //1 更新订单状态
        QueryWrapper<OrderInfo> wrapperOrder = new QueryWrapper<>();
        wrapperOrder.eq("out_trade_no",outTradeNo);
        OrderInfo orderInfo = orderInfoService.getOne(wrapperOrder);
        //状态已经支付
        orderInfo.setOrderStatus(OrderStatusEnum.PAID.getStatus());
        orderInfoService.updateById(orderInfo);
        //2 更新支付记录状态
        QueryWrapper<PaymentInfo> wrapperPayment = new QueryWrapper<>();
        wrapperPayment.eq("out_trade_no",outTradeNo);
        PaymentInfo paymentInfo = baseMapper.selectOne(wrapperPayment);
        //设置状态
        paymentInfo.setPaymentStatus(PaymentStatusEnum.PAID.getStatus());
        paymentInfo.setTradeNo(map.get("transaction_id"));
        paymentInfo.setCallbackTime(new Date());
        paymentInfo.setCallbackContent(map.toString());
        baseMapper.updateById(paymentInfo);

    }
}