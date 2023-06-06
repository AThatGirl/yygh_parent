package com.cj.yygh.orders.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.order.PaymentInfo;
import com.cj.yygh.model.order.RefundInfo;

/**
 * <p>
 * 退款信息表 服务类
 * </p>
 *
 * @author cj
 * @since 2023-06-06
 */
public interface RefundInfoService extends IService<RefundInfo> {

    RefundInfo saveRefund(PaymentInfo paymentInfo);
}
