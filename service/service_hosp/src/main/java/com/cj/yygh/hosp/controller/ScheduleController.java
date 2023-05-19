package com.cj.yygh.hosp.controller;

import com.cj.yygh.hosp.service.ScheduleService;
import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * ScheduleController
 * description:
 * 2023/5/19 10:36
 * Create by 杰瑞
 */
@RestController
@RequestMapping("/admin/hosp/schedule")
@Api(tags = "排班")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    //根据医院编号 、科室编号和工作日期，查询排班详细信息
    @ApiOperation(value = "查询排班详细信息")
    @GetMapping("/getScheduleDetail/{hoscode}/{depcode}/{workDate}")
    public R getScheduleDetail( @PathVariable(value = "hoscode") String hoscode,
                                @PathVariable(value = "depcode") String depcode,
                                @PathVariable(value = "workDate") String workDate) {
        List<Schedule> list = scheduleService.getDetailSchedule(hoscode,depcode,workDate);
        return R.ok().data("list",list);
    }

    @ApiOperation(value ="查询排班规则数据")
    @GetMapping("getScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public R getScheduleRule(@PathVariable long page,
                             @PathVariable long limit,
                             @PathVariable String hoscode,
                             @PathVariable String depcode) {
        Map<String,Object> map = scheduleService.getRuleSchedule(page,limit,hoscode,depcode);
        return R.ok().data(map);
    }


}
