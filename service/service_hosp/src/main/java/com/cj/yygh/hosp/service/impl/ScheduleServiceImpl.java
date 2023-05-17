package com.cj.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.yygh.constants.HospitalConstants;
import com.cj.yygh.hosp.repository.ScheduleRepository;
import com.cj.yygh.hosp.service.ScheduleService;
import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class ScheduleServiceImpl implements ScheduleService {


    @Autowired
    private ScheduleRepository scheduleRepository;


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
        if (delTarget != null){
            scheduleRepository.deleteById(delTarget.getId());
        }
    }
}
