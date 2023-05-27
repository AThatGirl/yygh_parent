package com.cj.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.user.Patient;

import java.util.List;

/**
 * <p>
 * 就诊人表 服务类
 * </p>
 *
 * @author cj
 * @since 2023-05-27
 */
public interface PatientService extends IService<Patient> {

    Patient getPatientInfo(Integer pid);

    List<Patient> selectPatientListByUid(Long userId);
}
