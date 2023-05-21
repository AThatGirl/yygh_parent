package com.cj.yygh.hosp.repository;

import com.cj.yygh.model.hosp.Department;
import com.cj.yygh.model.hosp.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * HospitalRepository
 * description:
 * 2023/5/15 20:05
 * Create by 杰瑞
 */
@Repository
public interface HospitalRepository extends MongoRepository<Hospital,String> {


    Hospital findByHoscode(String hoscode);


    List<Hospital> findByHosnameLike(String name);
}