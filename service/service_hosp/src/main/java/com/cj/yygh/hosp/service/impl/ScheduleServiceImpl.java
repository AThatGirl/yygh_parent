package com.cj.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.yygh.constants.HospitalConstants;
import com.cj.yygh.hosp.repository.ScheduleRepository;
import com.cj.yygh.hosp.service.HospitalService;
import com.cj.yygh.hosp.service.ScheduleService;
import com.cj.yygh.model.hosp.Hospital;
import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.vo.hosp.BookingScheduleRuleVo;
import com.cj.yygh.vo.hosp.ScheduleQueryVo;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private HospitalService hospitalService;


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

        List<Schedule> scheduleList = scheduleRepository.findByHoscodeAndDepcodeAndWorkDate(hoscode,depcode,new DateTime(workDate).toDate());
        return scheduleList;
    }

    /**
     * 根据日期获取周几数据
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
