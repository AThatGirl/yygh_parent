package com.cj.yygh.orders.controller;

import com.cj.yygh.enums.PaymentTypeEnum;
import com.cj.yygh.orders.service.PaymentService;
import com.cj.yygh.orders.service.WeixinService;
import com.cj.yygh.result.R;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/order/weixin")
public class WeixinController {
    @Autowired
    private WeixinService weixinPayService;

    @Autowired
    private PaymentService paymentService;

    @ApiOperation("根据订单id查询查询订单的支付状态")
    @GetMapping("/queryPayStatus/{orderId}")
    public R queryPayStatus(@PathVariable("orderId") Long orderId) {
        Map<String, String> map = weixinPayService.queryPayStatus(orderId);
        if (map == null) {
            return R.error().message("支付失败");
        }
        //支付成功，更新订单状态
        if ("SUCCESS".equals(map.get("trade_state"))) {
            //更新订单的状态
            //更新支付记录的状态
            String outTradeNo = map.get("out_trade_no");
            paymentService.paySuccess(outTradeNo, PaymentTypeEnum.WEIXIN.getStatus(), map);
            return R.ok().message("支付成功");
        }

        return R.ok().message("支付中");
    }

    /**
     * 下单 生成二维码
     */
    @GetMapping("/createNative/{orderId}")
    public R createNative(
            @ApiParam(name = "orderId", value = "订单id", required = true)
            @PathVariable("orderId") Long orderId) {
        Map map = weixinPayService.createNative(orderId);
        return R.ok().data(map);
    }


}