package com.cj.yygh.hosp.controller.api;

import com.cj.yygh.hosp.service.DepartmentService;
import com.cj.yygh.hosp.service.HospitalService;
import com.cj.yygh.hosp.service.ScheduleService;
import com.cj.yygh.model.hosp.Hospital;
import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.result.R;
import com.cj.yygh.vo.hosp.DepartmentVo;
import com.cj.yygh.vo.hosp.HospitalQueryVo;
import com.cj.yygh.vo.hosp.ScheduleOrderVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * HospitalApiController
 * description:
 * 2023/5/20 9:10
 * Create by 杰瑞
 */
@Api(tags = "医院显示接口")
@RestController
@RequestMapping("/api/hosp/hospital")
public class HospitalApiController {

    @Autowired
    private HospitalService hospitalService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "根据排班id获取预约下单数据")
    @GetMapping("/inner/getScheduleOrderVo/{scheduleId}")
    public ScheduleOrderVo getScheduleOrderVo(@PathVariable("scheduleId") String scheduleId) {
        return scheduleService.getScheduleOrderVo(scheduleId);
    }

    @ApiOperation("根据排班id查询排班信息")
    @GetMapping("/getSchedule/{id}")
    public R getScheduleInfo(@PathVariable("id") String scheduleId) {

        Schedule schedule = scheduleService.getScheduleInfo(scheduleId);
        return R.ok().data("schedule", schedule);
    }


    @ApiOperation(value = "获取排班数据")
    @GetMapping("/auth/findScheduleList/{hoscode}/{depcode}/{workDate}")
    public R findScheduleList(
            @PathVariable String hoscode,
            @PathVariable String depcode,
            @PathVariable String workDate) {
        List<Schedule> scheduleList = scheduleService.getDetailSchedule(hoscode, depcode, workDate);
        return R.ok().data("scheduleList", scheduleList);
    }

    @ApiOperation(value = "获取可预约排班数据")
    @GetMapping("/auth/getBookingScheduleRule/{page}/{limit}/{hoscode}/{depcode}")
    public R getBookingSchedule(@PathVariable("page") Integer page,
                                @PathVariable("limit") Integer limit,
                                @PathVariable("hoscode") String hoscode,
                                @PathVariable("depcode") String depcode) {

        Map<String, Object> map = scheduleService.getBookingSchedule(page, limit, hoscode, depcode);
        return R.ok().data(map);
    }


    @ApiOperation("根据医院编号查询医院信息")
    @GetMapping("/info/{hoscode}")
    public R info(@PathVariable(value = "hoscode") String hoscode) {

        return R.ok().data("hospital", hospitalService.findByHoscode(hoscode));
    }

    @ApiOperation("根据医院编号查询医院地下所有的科室信息")
    @GetMapping("/department/list/{hoscode}")
    public R getDepartmentByHoscode(@PathVariable(value = "hoscode") String hoscode) {
        List<DepartmentVo> allDepts = departmentService.getAllDepts(hoscode);
        return R.ok().data("list", allDepts);
    }

    @ApiOperation(value = "获取带查询条件的首页医院列表")
    @GetMapping("/{page}/{limit}")
    private R index(@PathVariable("page") Integer page, @PathVariable("limit") Integer limit, HospitalQueryVo queryVo) {

        Page<Hospital> hospitals = hospitalService.selectPage(page, limit, queryVo);
        return R.ok().data("pages", hospitals);
    }

    @ApiOperation(value = "根据医院名称模糊查询")
    @GetMapping("/findByNameLike/{name}")
    private R getHospitalPage(@PathVariable("name") String name) {

        List<Hospital> list = hospitalService.findByNameLike(name);

        return R.ok().data("list", list);
    }

}
