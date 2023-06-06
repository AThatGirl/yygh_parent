package com.cj.yygh.orders.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cj.yygh.enums.RefundStatusEnum;
import com.cj.yygh.model.order.PaymentInfo;
import com.cj.yygh.model.order.RefundInfo;
import com.cj.yygh.orders.mapper.RefundInfoMapper;
import com.cj.yygh.orders.service.RefundInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 退款信息表 服务实现类
 * </p>
 *
 * @author cj
 * @since 2023-06-06
 */
@Service
public class RefundInfoServiceImpl extends ServiceImpl<RefundInfoMapper, RefundInfo> implements RefundInfoService {

    @Override
    public RefundInfo saveRefund(PaymentInfo paymentInfo) {

        RefundInfo refundInfo = baseMapper.selectOne(new QueryWrapper<RefundInfo>().eq("order_id", paymentInfo.getOrderId()));
        if (refundInfo != null) {
            return refundInfo;
        }

        refundInfo = new RefundInfo();
        // 保存交易记录
        refundInfo = new RefundInfo();
        refundInfo.setCreateTime(new Date());
        refundInfo.setOrderId(paymentInfo.getOrderId());
        refundInfo.setPaymentType(paymentInfo.getPaymentType());
        refundInfo.setOutTradeNo(paymentInfo.getOutTradeNo());
        refundInfo.setTradeNo(paymentInfo.getTradeNo());
        refundInfo.setRefundStatus(RefundStatusEnum.UNREFUND.getStatus());
        refundInfo.setSubject(paymentInfo.getSubject());
        //paymentInfo.setSubject("test");
        refundInfo.setTotalAmount(paymentInfo.getTotalAmount());

        baseMapper.insert(refundInfo);
        return refundInfo;
    }
}
