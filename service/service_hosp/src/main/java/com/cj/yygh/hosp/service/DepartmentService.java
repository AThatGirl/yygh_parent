package com.cj.yygh.hosp.service;

import com.cj.yygh.model.hosp.Department;
import com.cj.yygh.vo.hosp.DepartmentVo;
import org.springframework.data.domain.Page;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * DepartmentService
 * description:
 * 2023/5/16 17:19
 * Create by 杰瑞
 */
public interface DepartmentService {

    void saveDepartment(Map<String, Object> stringObjectMap);

    Page<Department> findDepartmentPage(Map<String, Object> stringObjectMap);

    void remove(String hoscode, String depcode);

    List<DepartmentVo> getAllDepts(String hoscode);

}
