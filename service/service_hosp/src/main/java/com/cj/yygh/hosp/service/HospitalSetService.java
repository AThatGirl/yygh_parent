package com.cj.yygh.hosp.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.hosp.HospitalSet;

/**
 * <p>
 * 医院设置表 服务类
 * </p>
 *
 * @author cj
 * @since 2023-05-06
 */
public interface HospitalSetService extends IService<HospitalSet> {

    String getSignKey(String hoscode);
}
