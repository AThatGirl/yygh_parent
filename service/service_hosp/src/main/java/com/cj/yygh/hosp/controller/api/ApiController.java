package com.cj.yygh.hosp.controller.api;

import com.cj.yygh.exception.YyghException;
import com.cj.yygh.hosp.service.DepartmentService;
import com.cj.yygh.hosp.service.HospitalService;
import com.cj.yygh.hosp.service.HospitalSetService;
import com.cj.yygh.hosp.service.ScheduleService;
import com.cj.yygh.hosp.util.HttpRequestHelper;
import com.cj.yygh.hosp.util.MD5;
import com.cj.yygh.model.hosp.Department;
import com.cj.yygh.model.hosp.Hospital;
import com.cj.yygh.model.hosp.Schedule;
import com.cj.yygh.result.Result;
import com.cj.yygh.vo.hosp.ScheduleQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * HospitalController
 * description:
 * 2023/5/15 20:09
 * Create by 杰瑞
 */
@RequestMapping("/api/hosp")
@RestController
@Api(tags = "医院管理接口")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ScheduleService scheduleService;

    @ApiOperation(value = "根据医院id和排班id删除排班信息")
    @PostMapping("/schedule/remove")
    private Result removeScheduleByHospIdAndScheId(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());


        scheduleService.removeScheduleByHospIdAndScheId(paramMap);
        return Result.ok();
    }


    @ApiOperation(value = "查询排班分页")
    @PostMapping("/schedule/list")
    public Result getSchedulePage(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //TODO 校验参数
        String hoscode = (String) paramMap.get("hoscode");
        //非必填
        String depcode = (String) paramMap.get("depcode");
        int page = StringUtils.isEmpty(paramMap.get("page")) ? 1 : Integer.parseInt((String) paramMap.get("page"));
        int limit = StringUtils.isEmpty(paramMap.get("limit")) ? 10 : Integer.parseInt((String) paramMap.get("limit"));
        ScheduleQueryVo scheduleQueryVo = new ScheduleQueryVo();
        scheduleQueryVo.setHoscode(hoscode);
        scheduleQueryVo.setDepcode(depcode);
        Page<Schedule> pageModel = scheduleService.selectPage(page, limit, scheduleQueryVo);
        return Result.ok(pageModel);

    }


    @ApiOperation(value = "上传排班")
    @PostMapping("/saveSchedule")
    public Result saveSchedule(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //TODO 校验参数
        String hoscode = (String) paramMap.get("hoscode");
        scheduleService.save(paramMap);
        return Result.ok();
    }

    @ApiOperation(value = "删除科室")
    @PostMapping("/department/remove")
    public Result removeDepartment(HttpServletRequest request) {
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //TODO 参数校验
        String hoscode = (String) paramMap.get("hoscode");
        String depcode = (String) paramMap.get("depcode");
        departmentService.remove(hoscode, depcode);
        return Result.ok();
    }

    @ApiOperation("上传科室信息")
    @PostMapping("/department/list")
    public Result getDepartmentPage(HttpServletRequest request) {
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //TODO 校验

        Page<Department> page = departmentService.findDepartmentPage(stringObjectMap);
        return Result.ok(page);

    }


    @ApiOperation("上传科室信息")
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request) {
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //signKey TODO 校验
        departmentService.saveDepartment(stringObjectMap);
        return Result.ok();
    }


    @ApiOperation("查询医院")
    @PostMapping("/hospital/show")
    public Result show(HttpServletRequest request) {
        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //TODO 校验

        String hoscode = (String) stringObjectMap.get("hoscode");
        Hospital hospital = hospitalService.findByHoscode(hoscode);
        return Result.ok(hospital);
    }


    @ApiOperation("上传医院")
    @PostMapping("/saveHospital")
    public Result saveHospital(HttpServletRequest request) {

        Map<String, Object> stringObjectMap = HttpRequestHelper.switchMap(request.getParameterMap());

        //校验
        String sign = (String) stringObjectMap.get("sign");
        String plantFormSignKey = hospitalSetService.getSignKey((String) stringObjectMap.get("hoscode"));

        if (!StringUtils.isEmpty(sign) && !StringUtils.isEmpty(plantFormSignKey) && MD5.encrypt(plantFormSignKey).equals(sign)) {
            //传输过程中"+"转换为" "，
            String logoData = (String) stringObjectMap.get("logoData");
            logoData = logoData.replaceAll(" ", "+");
            stringObjectMap.put("logoData", logoData);
            hospitalService.save(stringObjectMap);
        } else {
            throw new YyghException(20001, "校验失败");
        }
        return Result.ok();
    }


}
