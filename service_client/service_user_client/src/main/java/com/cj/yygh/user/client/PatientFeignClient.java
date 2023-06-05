package com.cj.yygh.user.client;

import com.cj.yygh.model.user.Patient;
import com.cj.yygh.result.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * PatientFeignClient
 * description:
 * 2023/5/29 11:34
 * Create by 杰瑞
 */
@FeignClient(value = "service-user")
public interface PatientFeignClient {

    //获取就诊人
    @GetMapping("/api/userinfo/patient/auth/get/{id}")
    R getPatientInfo(@PathVariable("id") Integer id);
}