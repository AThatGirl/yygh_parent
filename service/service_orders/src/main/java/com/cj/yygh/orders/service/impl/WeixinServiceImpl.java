package com.cj.yygh.orders.service.impl;

import com.cj.yygh.enums.PaymentTypeEnum;
import com.cj.yygh.model.order.OrderInfo;
import com.cj.yygh.orders.service.OrderInfoService;
import com.cj.yygh.orders.service.PaymentService;
import com.cj.yygh.orders.service.WeixinService;
import com.cj.yygh.orders.utils.ConstantPropertiesUtils;
import com.cj.yygh.orders.utils.HttpClient;
import com.github.wxpay.sdk.WXPayUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * WeixinServiceImpl
 * description:
 * 2023/6/2 10:16
 * Create by 杰瑞
 */
@Service
public class WeixinServiceImpl implements WeixinService {

    @Autowired
    private OrderInfoService orderInfoService;

    @Autowired
    private PaymentService paymentService;


    @Override
    public Map createNative(Long orderId) {


        try {


            //根据订单id获取订单信息
            OrderInfo order = orderInfoService.getOrderInfo(orderId);
            //添加支付交易记录
            paymentService.savePaymentInfo(order, PaymentTypeEnum.WEIXIN.getStatus());
            //准备参数，调用微信服务器接口进行支付
            //1、设置参数
            Map paramMap = new HashMap();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());
            Date reserveDate = order.getReserveDate();
            String reserveDateString = new DateTime(reserveDate).toString("yyyy/MM/dd");
            String body = reserveDateString + "就诊" + order.getDepname();
            paramMap.put("body", body);
            paramMap.put("out_trade_no", order.getOutTradeNo());
            //paramMap.put("total_fee", order.getAmount().multiply(new BigDecimal("100")).longValue()+"");
            //TODO 测试使用的1分钱
            paramMap.put("total_fee", "1");//为了测试
            //终端ip
            paramMap.put("spbill_create_ip", "127.0.0.1");
            //回调地址
            paramMap.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify");
            paramMap.put("trade_type", "NATIVE");
            //2、HTTPClient来根据URL访问第三方接口并且传递参数

            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.post();
            String content = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(content);

            //4、封装返回结果集
            Map map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("totalFee", order.getAmount());
            map.put("resultCode", resultMap.get("result_code"));
            map.put("codeUrl", resultMap.get("code_url"));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, String> queryPayStatus(Long orderId) {

        try {

            OrderInfo orderInfo = orderInfoService.getOrderInfo(orderId);

            Map paramMap = new HashMap<>();
            paramMap.put("appid", ConstantPropertiesUtils.APPID);
            paramMap.put("mch_id", ConstantPropertiesUtils.PARTNER);
            paramMap.put("out_trade_no", orderInfo.getOutTradeNo());
            paramMap.put("nonce_str", WXPayUtil.generateNonceStr());

            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setXmlParam(WXPayUtil.generateSignedXml(paramMap, ConstantPropertiesUtils.PARTNERKEY));
            httpClient.setHttps(true);
            httpClient.post();
            String content = httpClient.getContent();
            return WXPayUtil.xmlToMap(content);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
