package com.cj.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.yygh.constants.HospitalConstants;
import com.cj.yygh.exception.YyghException;
import com.cj.yygh.hosp.repository.ScheduleRepository;
import com.cj.yygh.hosp.service.DepartmentService;
import com.cj.yygh.hosp.service.HospitalService;
import com.cj.yygh.hosp.service.ScheduleService;
import com.cj.yygh.model.hosp.BookingRule;
import com.cj.yygh.model.hosp.Department;
import com.cj.yygh.model.hosp.Hospital;
import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.vo.hosp.BookingScheduleRuleVo;
import com.cj.yygh.vo.hosp.ScheduleOrderVo;
import com.cj.yygh.vo.hosp.ScheduleQueryVo;
import com.cj.yygh.vo.order.OrderMqVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;


    @Override
    public void save(Map<String, Object> paramMap) {
        //查询mongodb是否有排班信息
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);
        //根据医院编号 和 排班编号查询
        Schedule scheduleExist = scheduleRepository.
                findByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHoscode());
        //判断
        if (scheduleExist != null) {
            schedule.setUpdateTime(new Date());
            schedule.setId(scheduleExist.getId());
            schedule.setIsDeleted(scheduleExist.getIsDeleted());
            schedule.setCreateTime(scheduleExist.getCreateTime());
        } else {
            schedule.setCreateTime(new Date());
            schedule.setUpdateTime(new Date());
            schedule.setIsDeleted(HospitalConstants.DELETE_NOT);
        }
        schedule.setStatus(HospitalConstants.ONLINE_YES);
        scheduleRepository.save(schedule);
    }

    @Override
    public Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo) {

        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleQueryVo, schedule);
        //查询未删除的
        schedule.setIsDeleted(0);
        PageRequest pageObj = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "createTime"));
        Example<Schedule> example = Example.of(schedule);
        return scheduleRepository.findAll(example, pageObj);
    }

    @Override
    public void removeScheduleByHospIdAndScheId(Map<String, Object> paramMap) {
        //查询mongodb是否有排班信息
        Schedule schedule = JSONObject.parseObject(JSONObject.toJSONString(paramMap), Schedule.class);
        Schedule delTarget = scheduleRepository.findByHoscodeAndHosScheduleId(schedule.getHoscode(), schedule.getHosScheduleId());
        if (delTarget != null) {
            scheduleRepository.deleteById(delTarget.getId());
        }
    }

    @Override
    public Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode) {
        Map<String, Object> map = new HashMap<>();
        //获取当前页的列表
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation aggregation = Aggregation.newAggregation(
                Aggregation.match(criteria),//设置聚合条件
                Aggregation.group("workDate") //根据workDate分组
                        .first("workDate").as("workDate") //查询这个字段，起别名
                        .sum("reservedNumber").as("reservedNumber") //对总的可预约数求和
                        .sum("availableNumber").as("availableNumber"),//对剩余可预约数求和
                Aggregation.sort(Sort.Direction.ASC, "workDate"),
                Aggregation.skip((page - 1) * limit),
                Aggregation.limit(limit)
        );
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(aggregation, Schedule.class, BookingScheduleRuleVo.class);
        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        for (BookingScheduleRuleVo mappedResult : mappedResults) {
            //获取周
            String dayOfWeek = this.getDayOfWeek(new DateTime(mappedResult.getWorkDate()));
            mappedResult.setDayOfWeek(dayOfWeek);
        }

        map.put("bookingScheduleRuleList", mappedResults);
        //获取总记录数
        Criteria criteria1 = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode);
        Aggregation aggregation1 = Aggregation.newAggregation(
                Aggregation.match(criteria1),//设置聚合条件
                Aggregation.group("workDate") //根据workDate分组
        );
        AggregationResults<BookingScheduleRuleVo> aggregate1 = mongoTemplate.aggregate(aggregation1, Schedule.class, BookingScheduleRuleVo.class);
        int total = aggregate1.getMappedResults().size();
        map.put("total", total);

        //获取医院名称
        Hospital hospital = hospitalService.findByHoscode(hoscode);
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        baseMap.put("hosname", hospital.getHosname());
        map.put("baseMap", baseMap);
        return map;
    }

    @Override
    public List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate) {

        List<Schedule> scheduleList = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode, depcode, new DateTime(workDate).toDate());
        return scheduleList;
    }

    @Override
    public Map<String, Object> getBookingSchedule(Integer page, Integer limit, String hoscode, String depcode) {
        //根据医院编号获取医院信息
        Hospital hospital = hospitalService.findByHoscode(hoscode);
        if (hospital == null) {
            throw new YyghException(20001, "没有相关医院信息");
        }
        BookingRule bookingRule = hospital.getBookingRule();
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> dataPage = this.getListPage(page, limit, bookingRule);

        //获取当前页时间列表
        List<Date> records = dataPage.getRecords();
        //获取可预约日期科室剩余预约数
        Criteria criteria = Criteria.where("hoscode").is(hoscode).and("depcode").is(depcode).and("workDate").in(records);
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.match(criteria),
                Aggregation.group("workDate")//分组字段
                        .first("workDate").as("workDate")
                        .count().as("docCount")
                        .sum("availableNumber").as("availableNumber")
                        .sum("reservedNumber").as("reservedNumber")
        );
        AggregationResults<BookingScheduleRuleVo> aggregate = mongoTemplate.aggregate(agg, Schedule.class, BookingScheduleRuleVo.class);
        //聚合列表
        List<BookingScheduleRuleVo> mappedResults = aggregate.getMappedResults();
        //转为map类型
        Map<Date, BookingScheduleRuleVo> collect = mappedResults.stream().collect(Collectors.toMap(BookingScheduleRuleVo::getWorkDate, bookingScheduleRuleVo -> bookingScheduleRuleVo));

        List<BookingScheduleRuleVo> newList = new ArrayList<>();
        for (int i = 0; i < records.size(); i++) {
            Date date = records.get(i);
            BookingScheduleRuleVo bookingScheduleRuleVo = collect.get(date);
            if (bookingScheduleRuleVo == null) {
                bookingScheduleRuleVo = new BookingScheduleRuleVo();
                //就诊医生人数
                bookingScheduleRuleVo.setDocCount(HospitalConstants.DOCTOR_NUMBER_ZERO);
                //-1表示无号
                bookingScheduleRuleVo.setAvailableNumber(HospitalConstants.AVAILABLE_NUMBER_NOT);

            }
            bookingScheduleRuleVo.setWorkDate(date);
            bookingScheduleRuleVo.setWorkDateMd(date);
            //计算当前预约日期为周几
            String dayOfWeek = this.getDayOfWeek(new DateTime(date));
            bookingScheduleRuleVo.setDayOfWeek(dayOfWeek);

            //最后一页最后一条记录为即将预约   状态 0：正常 1：即将放号 -1：当天已停止挂号
            int len = records.size();
            if (i == len - 1 && page == dataPage.getPages()) {
                bookingScheduleRuleVo.setStatus(HospitalConstants.APPOINTMENT_STATUS_SOON);
            } else {
                bookingScheduleRuleVo.setStatus(HospitalConstants.APPOINTMENT_STATUS_NORMAL);
            }
            //判断第一页第一条的时间是否已经过了当天预约放号时间
            if (i == 0 && page == 1) {
                //获取今天放号时间
                DateTime dateTime = this.getDateTime(new Date(), bookingRule.getStopTime());
                if (dateTime.isBeforeNow()) {
                    bookingScheduleRuleVo.setStatus(HospitalConstants.APPOINTMENT_STATUS_STOP);
                }
            }
            newList.add(bookingScheduleRuleVo);

        }

        Map<String, Object> result = new HashMap<>();
        //可预约日期规则数据
        result.put("bookingScheduleList", newList);
        result.put("total", dataPage.getTotal());
        //其他基础数据
        Map<String, String> baseMap = new HashMap<>();
        //医院名称
        baseMap.put("hosname", hospitalService.findByHoscode(hoscode).getHosname());
        //科室
        Department department = departmentService.getDepartment(hoscode, depcode);
        //大科室名称
        baseMap.put("bigname", department.getBigname());
        //科室名称
        baseMap.put("depname", department.getDepname());
        //月
        baseMap.put("workDateString", new DateTime().toString("yyyy年MM月"));
        //放号时间
        baseMap.put("releaseTime", bookingRule.getReleaseTime());
        //停号时间
        baseMap.put("stopTime", bookingRule.getStopTime());
        result.put("baseMap", baseMap);
        Set<String> keySet = result.keySet();
        return result;
    }

    @Override
    public Schedule getScheduleInfo(String scheduleId) {

        Schedule schedule = scheduleRepository.findById(scheduleId).get();
        this.packageSchedule(schedule);
        return schedule;
    }

    @Override
    public ScheduleOrderVo getScheduleOrderVo(String scheduleId) {
        ScheduleOrderVo scheduleOrderVo = new ScheduleOrderVo();
        //排班信息
        Schedule schedule = this.getScheduleInfo(scheduleId);
        if (null == schedule) {
            throw new YyghException();
        }
        //获取预约规则信息
        Hospital hospital = hospitalService.findByHoscode(schedule.getHoscode());
        if (null == hospital) {
            throw new YyghException();
        }

        BookingRule bookingRule = hospital.getBookingRule();
        if (null == bookingRule) {
            throw new YyghException();
        }

        scheduleOrderVo.setHoscode(schedule.getHoscode());
        scheduleOrderVo.setHosname(hospital.getHosname());
        scheduleOrderVo.setDepcode(schedule.getDepcode());
        scheduleOrderVo.setDepname(departmentService.getDepartment(schedule.getHoscode(), schedule.getDepcode()).getDepname());
        scheduleOrderVo.setHosScheduleId(schedule.getHosScheduleId());
        scheduleOrderVo.setAvailableNumber(schedule.getAvailableNumber());
        scheduleOrderVo.setTitle(schedule.getTitle());
        scheduleOrderVo.setReserveDate(schedule.getWorkDate());
        scheduleOrderVo.setReserveTime(schedule.getWorkTime());
        scheduleOrderVo.setAmount(schedule.getAmount());
        //退号截止天数（如：就诊前一天为-1，当天为0）
        int quitDay = bookingRule.getQuitDay();
        DateTime quitTime = this.getDateTime(new DateTime(schedule.getWorkDate()).plusDays(quitDay).toDate(), bookingRule.getQuitTime());
        scheduleOrderVo.setQuitTime(quitTime.toDate());
        //预约开始时间
        DateTime startTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        scheduleOrderVo.setStartTime(startTime.toDate());
        //预约截止时间
        DateTime endTime = this.getDateTime(new DateTime().plusDays(bookingRule.getCycle()).toDate(), bookingRule.getStopTime());
        scheduleOrderVo.setEndTime(endTime.toDate());
        //当天停止挂号时间
        DateTime stopTime = this.getDateTime(new Date(), bookingRule.getStopTime());
        scheduleOrderVo.setStopTime(stopTime.toDate());
        return scheduleOrderVo;
    }

    @Override
    public void update(OrderMqVo orderMqVo) {

        Schedule schedule = null;
        if (orderMqVo.getAvailableNumber() != null) {
            schedule = scheduleRepository.findById(orderMqVo.getScheduleId()).get();
            schedule.setReservedNumber(orderMqVo.getReservedNumber());
            schedule.setAvailableNumber(orderMqVo.getAvailableNumber());
        } else {
            schedule = scheduleRepository.findByHosScheduleId(orderMqVo.getScheduleId());
            schedule.setAvailableNumber(schedule.getAvailableNumber() + 1);
        }
        scheduleRepository.save(schedule);
    }

    private void packageSchedule(Schedule schedule) {

        Hospital hospital = hospitalService.findByHoscode(schedule.getHoscode());

        Department department = departmentService.getDepartment(schedule.getHoscode(), schedule.getDepcode());
        schedule.getParam().put("hosname", hospital.getHosname());
        schedule.getParam().put("depname", department.getDepname());
        schedule.getParam().put("dayOfWeek", this.getDayOfWeek(new DateTime(schedule.getWorkDate())));
    }

    private com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> getListPage(Integer page, Integer limit, BookingRule bookingRule) {
        //获取今天的放号时间
        DateTime dateTime = this.getDateTime(new Date(), bookingRule.getReleaseTime());
        //如果当前时间过了当天放号时间，就把预约周期+1
        Integer cycle = bookingRule.getCycle();
        if (dateTime.isBeforeNow()) {
            cycle = cycle + 1;
        }

        List<Date> dateList = new ArrayList<>();
        for (int i = 0; i < cycle; i++) {
            DateTime tmpTime = new DateTime().plusDays(i);
            dateList.add(new DateTime(tmpTime.toString("yyyy-MM-dd")).toDate());
        }

        int start = (page - 1) * limit;
        int end = start + limit;
        if (end > dateList.size()) {
            end = dateList.size();
        }
        List<Date> currentPageDataList = new ArrayList<>();
        for (int i = start; i < end; i++) {
            Date date = dateList.get(i);
            currentPageDataList.add(date);
        }

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Date> resultPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(page, limit, dateList.size());
        resultPage.setRecords(currentPageDataList);
        return resultPage;
    }

    /**
     * 将Date日期（yyyy-MM-dd HH:mm）转换为DateTime
     */
    private DateTime getDateTime(Date date, String timeString) {
        String dateTimeString = new DateTime(date).toString("yyyy-MM-dd") + " " + timeString;
        DateTime dateTime = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm").parseDateTime(dateTimeString);
        return dateTime;
    }

    /**
     * 根据日期获取周几数据
     *
     * @param dateTime
     * @return
     */
    private String getDayOfWeek(DateTime dateTime) {
        String dayOfWeek = "";
        switch (dateTime.getDayOfWeek()) {
            case DateTimeConstants.SUNDAY:
                dayOfWeek = "周日";
                break;
            case DateTimeConstants.MONDAY:
                dayOfWeek = "周一";
                break;
            case DateTimeConstants.TUESDAY:
                dayOfWeek = "周二";
                break;
            case DateTimeConstants.WEDNESDAY:
                dayOfWeek = "周三";
                break;
            case DateTimeConstants.THURSDAY:
                dayOfWeek = "周四";
                break;
            case DateTimeConstants.FRIDAY:
                dayOfWeek = "周五";
                break;
            case DateTimeConstants.SATURDAY:
                dayOfWeek = "周六";
            default:
                break;
        }
        return dayOfWeek;
    }


}
