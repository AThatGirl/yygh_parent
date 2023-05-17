package com.cj.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.yygh.constants.HospitalConstants;
import com.cj.yygh.hosp.repository.HospitalRepository;
import com.cj.yygh.hosp.service.HospitalService;
import com.cj.yygh.model.hosp.Department;
import com.cj.yygh.model.hosp.Hospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * HospitalServiceImpl
 * description:
 * 2023/5/15 20:08
 * Create by 杰瑞
 */
@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalRepository hospitalRepository;


    @Override
    public void save(Map<String, Object> stringObjectMap) {
        Hospital hospital = JSONObject.parseObject(JSONObject.toJSONString(stringObjectMap), Hospital.class);
        //根据医院编号查询医院信息
        Hospital mongoHospital = hospitalRepository.findByHoscode(hospital.getHoscode());
        //如果没有医院信息，就添加
        if (mongoHospital == null) {
            hospital.setStatus(HospitalConstants.ONLINE_NOT);
            hospital.setCreateTime(new Date());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(HospitalConstants.ONLINE_NOT);
            hospitalRepository.save(hospital);
        } else {
            //如果有医院信息，就更新
            hospital.setStatus(mongoHospital.getStatus());
            hospital.setCreateTime(mongoHospital.getCreateTime());
            hospital.setUpdateTime(new Date());
            hospital.setIsDeleted(mongoHospital.getIsDeleted());
            hospital.setId(mongoHospital.getId());

        }


    }

    @Override
    public Hospital findByHoscode(String hoscode) {
        return hospitalRepository.findByHoscode(hoscode);
    }

}
