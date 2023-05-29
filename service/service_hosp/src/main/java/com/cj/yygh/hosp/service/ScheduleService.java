package com.cj.yygh.hosp.service;

import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    void save(Map<String, Object> paramMap);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void removeScheduleByHospIdAndScheId(Map<String, Object> paramMap);

    Map<String, Object> getRuleSchedule(long page, long limit, String hoscode, String depcode);


    List<Schedule> getDetailSchedule(String hoscode, String depcode, String workDate);

    Map<String, Object> getBookingSchedule(Integer page, Integer limit, String hoscode, String depcode);

    Schedule getScheduleInfo(String scheduleId);

}