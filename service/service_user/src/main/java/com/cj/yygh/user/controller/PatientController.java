package com.cj.yygh.user.controller;


import com.cj.yygh.model.user.Patient;
import com.cj.yygh.result.R;
import com.cj.yygh.user.service.PatientService;
import com.cj.yygh.user.utils.AuthContextHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 就诊人表 前端控制器
 * </p>
 *
 * @author cj
 * @since 2023-05-27
 */
@RestController
@RequestMapping("/api/userinfo/patient")
@Api(tags = "就诊")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @ApiOperation("添加就诊人")
    @PostMapping("/auth/save")
    public R savePatient(@RequestBody Patient patient, HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        patient.setUserId(userId);
        patientService.save(patient);
        return R.ok();
    }

    @ApiOperation("删除就诊人信息")
    @DeleteMapping("/auth/remove/{id}")
    public R removeById(@PathVariable("id") Integer pid) {
        patientService.removeById(pid);
        return R.ok();
    }

    @ApiOperation("获取就诊人信息")
    @GetMapping("/auth/get/{id}")
    public R getPatientInfo(@PathVariable("id") Integer pid) {

        Patient patient = patientService.getPatientInfo(pid);
        return R.ok().data("patient", patient);
    }

    @ApiOperation("修改就诊人信息")
    @PutMapping("/auth/update")
    public R updatePatient(@RequestBody Patient patient) {
        patientService.updateById(patient);
        return R.ok();
    }

    @ApiOperation("查询当前登录用户底下所有的就诊人信息")
    @GetMapping("/auth/findAll")
    public R getPatientList(HttpServletRequest request) {

        Long userId = AuthContextHolder.getUserId(request);
        List<Patient> list = patientService.selectPatientListByUid(userId);
        return R.ok().data("list", list);
    }


}

