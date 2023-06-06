package com.cj.yygh.orders.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cj.yygh.model.order.OrderInfo;
import com.cj.yygh.vo.order.OrderCountQueryVo;
import com.cj.yygh.vo.order.OrderCountVo;

import java.util.List;

/**
 * <p>
 * 订单表 Mapper 接口
 * </p>
 *
 * @author cj
 * @since 2023-05-29
 */
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {

    List<OrderCountVo> countOrderInfoByQuery(OrderCountQueryVo orderCountQueryVo);
}
