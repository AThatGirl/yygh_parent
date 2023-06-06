package com.cj.yygh.orders.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cj.yygh.enums.OrderStatusEnum;
import com.cj.yygh.enums.PaymentStatusEnum;
import com.cj.yygh.exception.YyghException;
import com.cj.yygh.hosp.client.HospitalFeignClient;
import com.cj.yygh.model.order.OrderInfo;
import com.cj.yygh.model.order.PaymentInfo;
import com.cj.yygh.model.user.Patient;
import com.cj.yygh.orders.mapper.OrderInfoMapper;
import com.cj.yygh.orders.service.OrderInfoService;
import com.cj.yygh.orders.service.PaymentService;
import com.cj.yygh.orders.service.WeixinService;
import com.cj.yygh.orders.utils.HttpRequestHelper;
import com.cj.yygh.rabbit.MqConst;
import com.cj.yygh.rabbit.RabbitService;
import com.cj.yygh.result.R;
import com.cj.yygh.user.client.PatientFeignClient;
import com.cj.yygh.vo.hosp.ScheduleOrderVo;
import com.cj.yygh.vo.msm.MsmVo;
import com.cj.yygh.vo.order.OrderCountQueryVo;
import com.cj.yygh.vo.order.OrderCountVo;
import com.cj.yygh.vo.order.OrderMqVo;
import com.cj.yygh.vo.order.OrderQueryVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author cj
 * @since 2023-05-29
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {


    @Autowired
    private HospitalFeignClient hospitalFeignClient;

    @Autowired
    private PatientFeignClient patientFeignClient;

    @Autowired
    private RabbitService rabbitService;

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private PaymentService paymentService;


    @Override
    public Long saveOrder(String scheduleId, Integer patientId) {
        //先根据scheduleId获取医生的排班信息
        ScheduleOrderVo scheduleOrderVo = hospitalFeignClient.getScheduleOrderVo(scheduleId);
        //根据patientId获取就诊人信息
        R r = patientFeignClient.getPatientInfo(patientId);

        Patient patient = JSONObject.parseObject(JSONObject.toJSONString(r.getData().get("patient")), Patient.class);


        //平台系统调用第三方医院系统：确认是否还能挂号
        // 3.1 如果医院返回失败，挂号失败
        //使用map集合封装需要传过医院数据
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("hoscode", scheduleOrderVo.getHoscode());
        paramMap.put("depcode", scheduleOrderVo.getDepcode());
        paramMap.put("hosScheduleId", scheduleOrderVo.getHosScheduleId());
        paramMap.put("reserveDate", new DateTime(scheduleOrderVo.getReserveDate()).toString("yyyy-MM-dd"));
        paramMap.put("reserveTime", scheduleOrderVo.getReserveTime());

        paramMap.put("amount", scheduleOrderVo.getAmount()); //挂号费用
        paramMap.put("name", patient.getName());
        paramMap.put("certificatesType", patient.getCertificatesType());
        paramMap.put("certificatesNo", patient.getCertificatesNo());
        paramMap.put("sex", patient.getSex());
        paramMap.put("birthdate", patient.getBirthdate());
        paramMap.put("phone", patient.getPhone());
        paramMap.put("isMarry", patient.getIsMarry());
        paramMap.put("provinceCode", patient.getProvinceCode());
        paramMap.put("cityCode", patient.getCityCode());
        paramMap.put("districtCode", patient.getDistrictCode());
        paramMap.put("address", patient.getAddress());
        //联系人
        paramMap.put("contactsName", patient.getContactsName());
        paramMap.put("contactsCertificatesType", patient.getContactsCertificatesType());
        paramMap.put("contactsCertificatesNo", patient.getContactsCertificatesNo());
        paramMap.put("contactsPhone", patient.getContactsPhone());
        paramMap.put("timestamp", HttpRequestHelper.getTimestamp());
        //String sign = HttpRequestHelper.getSign(paramMap, signInfoVo.getSignKey());
        paramMap.put("sign", "");
        JSONObject jsonObject = HttpRequestHelper.sendRequest(paramMap, "http://localhost:9998/order/submitOrder");
        int code = jsonObject.getIntValue("code");
        if (code == 200) {
            //预约挂号成功，保存新订单
            OrderInfo orderInfo = new OrderInfo();
            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
            //预约记录唯一标识（医院预约记录主键）
            String hosRecordId = jsonObject1.getString("hosRecordId");
            //预约序号
            Integer number = jsonObject1.getInteger("number");
            ;
            //取号时间
            String fetchTime = jsonObject1.getString("fetchTime");
            ;
            //取号地址
            String fetchAddress = jsonObject1.getString("fetchAddress");
            ;

            //设置添加数据--排班数据
            BeanUtils.copyProperties(scheduleOrderVo, orderInfo);
            //设置添加数据--就诊人数据
            //订单号
            String outTradeNo = System.currentTimeMillis() + "" + new Random().nextInt(100);
            orderInfo.setOutTradeNo(outTradeNo);
            orderInfo.setScheduleId(scheduleOrderVo.getHosScheduleId());
            orderInfo.setUserId(patient.getUserId());
            orderInfo.setPatientId(patientId.longValue());
            orderInfo.setPatientName(patient.getName());
            orderInfo.setPatientPhone(patient.getPhone());
            orderInfo.setOrderStatus(OrderStatusEnum.UNPAID.getStatus());

            //设置添加数据--医院接口返回数据
            orderInfo.setHosRecordId(hosRecordId);
            orderInfo.setNumber(number);
            orderInfo.setFetchTime(fetchTime);
            orderInfo.setFetchAddress(fetchAddress);
            baseMapper.insert(orderInfo);
            //更新排班预约人数
            Integer availableNumber = jsonObject1.getInteger("availableNumber");
            Integer reservedNumber = jsonObject1.getInteger("reservedNumber");

            //发给就诊人预约成功 短信提示
            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setReservedNumber(reservedNumber);
            orderMqVo.setAvailableNumber(availableNumber);
            orderMqVo.setScheduleId(scheduleId);

            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(patient.getPhone());
            msmVo.setTemplateCode("您{code}预约的号议程");
            orderMqVo.setMsmVo(msmVo);
            //发送
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);

            return orderInfo.getId();
        } else {
            throw new YyghException(20001, "挂号异常");
        }

    }

    //实现列表
//（条件查询带分页）
    @Override
    public IPage<OrderInfo> selectPage(Page<OrderInfo> pageParam, OrderQueryVo orderQueryVo) {
        //orderQueryVo获取条件值
        String name = orderQueryVo.getKeyword(); //医院名称
        Long patientId = orderQueryVo.getPatientId(); //就诊人名称
        String orderStatus = orderQueryVo.getOrderStatus(); //订单状态
        String reserveDate = orderQueryVo.getReserveDate();//安排时间
        String createTimeBegin = orderQueryVo.getCreateTimeBegin();
        String createTimeEnd = orderQueryVo.getCreateTimeEnd();
        //对条件值进行非空判断
        QueryWrapper<OrderInfo> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("hosname", name);
        }
        if (!StringUtils.isEmpty(patientId)) {
            wrapper.eq("patient_id", patientId);
        }
        if (!StringUtils.isEmpty(orderStatus)) {
            wrapper.eq("order_status", orderStatus);
        }
        if (!StringUtils.isEmpty(reserveDate)) {
            wrapper.ge("reserve_date", reserveDate);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge("create_time", createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le("create_time", createTimeEnd);
        }
        //调用mapper的方法
        IPage<OrderInfo> pages = baseMapper.selectPage(pageParam, wrapper);
        //编号变成对应值封装
        pages.getRecords().stream().forEach(item -> {
            this.packOrderInfo(item);
        });
        return pages;
    }

    @Override
    public OrderInfo getOrderInfo(Long orderId) {
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        return this.packOrderInfo(orderInfo);
    }

    @Override
    public boolean cancelOrder(Integer orderId) {
        //判断当前时间是否已经过了平台规定的退号截止时间
        OrderInfo orderInfo = baseMapper.selectById(orderId);
        DateTime dateTime = new DateTime(orderInfo.getQuitTime());
        if (dateTime.isBeforeNow()) {
            throw new YyghException(20001, "已过退号截止时间");
        }
        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("hoscode", orderInfo.getHoscode());
        reqMap.put("hosRecordId", orderInfo.getHosRecordId());
        reqMap.put("timestamp", HttpRequestHelper.getTimestamp());
        //reqMap.put("sign", "");
        //2.当前时间没有过退号截止时间，平台系统调用医院系统，确认能否取消预约

        JSONObject jsonObject = HttpRequestHelper.sendRequest(reqMap, "http://localhost:9998/order/updateCancelStatus");
        //如果医院返回不能取消，抛出异常
        if (jsonObject.getInteger("code") != 200) {
            throw new YyghException(20001, "不能取消");
        } else {
            //如果可以取消，取消
            boolean flag = weixinService.refund(orderId);
            if (!flag){
                throw new YyghException(20001, "退款失败");
            }




            //更新订单表状态，支付记录表的支付状态
            orderInfo.setOrderStatus(OrderStatusEnum.CANCLE.getStatus());
            this.updateById(orderInfo);
            //更新支付记录状态。
            QueryWrapper<PaymentInfo> queryWrapper=new QueryWrapper<PaymentInfo>();
            queryWrapper.eq("order_id",orderId);
            PaymentInfo paymentInfo = paymentService.getOne(queryWrapper);
            paymentInfo.setPaymentStatus(PaymentStatusEnum.REFUND.getStatus()); //退款
            paymentInfo.setUpdateTime(new Date());
            paymentService.updateById(paymentInfo);
            //更新排班数据+1，发送短信提示
            //发送mq信息更新预约数 我们与下单成功更新预约数使用相同的mq信息，不设置可预约数与剩余预约数，接收端可预约数减1即可
            OrderMqVo orderMqVo = new OrderMqVo();
            orderMqVo.setScheduleId(orderInfo.getScheduleId());
            //短信提示
            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(orderInfo.getPatientPhone());
            orderMqVo.setMsmVo(msmVo);
            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_ORDER, MqConst.ROUTING_ORDER, orderMqVo);

            return true;
        }


    }

    @Override
    public void patientTips() {
        String string = new DateTime().toString("yyyy-MM-dd");
        List<OrderInfo> list = baseMapper.selectList(new QueryWrapper<OrderInfo>().eq("reserve_date", string).ne("order_status", OrderStatusEnum.CANCLE.getStatus()));
        for (OrderInfo orderInfo : list) {
            MsmVo msmVo = new MsmVo();
            msmVo.setPhone(orderInfo.getPatientPhone());

            rabbitService.sendMessage(MqConst.EXCHANGE_DIRECT_MSM, MqConst.ROUTING_MSM_ITEM, msmVo);

        }

    }

    @Override
    public Map<String, Object>  countOrderInfoByQuery(OrderCountQueryVo orderCountQueryVo) {
        List<OrderCountVo> orderCountVos = baseMapper.countOrderInfoByQuery(orderCountQueryVo);
        List<String> dataList = orderCountVos.stream().map(OrderCountVo::getReserveDate).collect(Collectors.toList());
        List<Integer> countList = orderCountVos.stream().map(OrderCountVo::getCount).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("dateList", dataList);
        map.put("countList", countList);
        return map;
    }

    private OrderInfo packOrderInfo(OrderInfo orderInfo) {
        orderInfo.getParam().put("orderStatusString", OrderStatusEnum.getStatusNameByStatus(orderInfo.getOrderStatus()));
        return orderInfo;
    }
}
