package com.cj.yygh.hosp.service;

import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.vo.hosp.ScheduleQueryVo;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ScheduleService {

    void save(Map<String, Object> paramMap);

    Page<Schedule> selectPage(int page, int limit, ScheduleQueryVo scheduleQueryVo);

    void removeScheduleByHospIdAndScheId(Map<String, Object> paramMap);
}