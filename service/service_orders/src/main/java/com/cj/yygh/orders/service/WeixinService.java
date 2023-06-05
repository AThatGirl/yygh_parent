package com.cj.yygh.orders.service;

import java.util.Map;

/**
 * WeixinService
 * description:
 * 2023/6/2 10:16
 * Create by 杰瑞
 */
public interface WeixinService {
    Map createNative(Long orderId);

    Map<String, String> queryPayStatus(Long orderId);
}
