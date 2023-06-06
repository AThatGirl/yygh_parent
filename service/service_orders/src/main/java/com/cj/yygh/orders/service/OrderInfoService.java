package com.cj.yygh.orders.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.order.OrderInfo;
import com.cj.yygh.vo.order.OrderCountQueryVo;
import com.cj.yygh.vo.order.OrderCountVo;
import com.cj.yygh.vo.order.OrderQueryVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author cj
 * @since 2023-05-29
 */
public interface OrderInfoService extends IService<OrderInfo> {

    Long saveOrder(String scheduleId, Integer patientId);

    IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo);

    OrderInfo getOrderInfo(Long orderId);

    boolean cancelOrder(Integer orderId);

    void patientTips();

    Map<String, Object>  countOrderInfoByQuery(OrderCountQueryVo orderCountQueryVo);

}
