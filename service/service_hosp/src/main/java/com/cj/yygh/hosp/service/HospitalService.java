package com.cj.yygh.hosp.service;

import com.cj.yygh.model.hosp.Hospital;
import com.cj.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.data.domain.Page;

import java.util.List;
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


    Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo);

    void updateStatus(String id, Integer status);

    Map<String, Object> getHospitalDetailById(String id);

    List<Hospital> findByNameLike(String name);
}
