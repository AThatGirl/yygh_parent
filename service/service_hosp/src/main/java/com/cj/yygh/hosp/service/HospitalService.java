package com.cj.yygh.hosp.service;

import com.cj.yygh.model.hosp.Hospital;

import java.util.Map;

/**
 * HospitalSerivce
 * description:
 * 2023/5/15 20:07
 * Create by 杰瑞
 */
public interface HospitalService {


    void save(Map<String, Object> stringObjectMap);

    Hospital findByHoscode(String hoscode);


}
