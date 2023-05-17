package com.cj.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.yygh.constants.HospitalConstants;
import com.cj.yygh.hosp.repository.DepartmentRepository;
import com.cj.yygh.hosp.service.DepartmentService;
import com.cj.yygh.model.hosp.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * DepartmentServiceImpl
 * description:
 * 2023/5/16 17:19
 * Create by 杰瑞
 */
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public void saveDepartment(Map<String, Object> stringObjectMap) {
        Department department = JSONObject.parseObject(JSONObject.toJSONString(stringObjectMap), Department.class);

        //查询mongodb是否有科室信息
        Department targetDepartment = departmentRepository.findByHoscodeAndDepcode(department.getHoscode(), department.getDepcode());
        //有，做更新操作，没有，做更新操作
        if (targetDepartment == null) {
            department.setCreateTime(new Date());
            department.setUpdateTime(new Date());
            department.setIsDeleted(HospitalConstants.DELETE_NOT);
        } else {
            department.setCreateTime(targetDepartment.getCreateTime());
            department.setUpdateTime(new Date());
            department.setIsDeleted(targetDepartment.getIsDeleted());
            department.setId(targetDepartment.getId());
        }
        departmentRepository.save(department);
    }

    @Override
    public Page<Department> findDepartmentPage(Map<String, Object> stringObjectMap) {

        String hoscode = (String) stringObjectMap.get("hoscode");
        Department department = new Department();
        department.setHoscode(hoscode);
        //设置需要查询的属性
        Example<Department> example = Example.of(department);
        //分页查询，并且根据createTime排序
        PageRequest pageRequest = PageRequest.of(Integer.parseInt((String) stringObjectMap.get("page")) - 1,
                Integer.parseInt((String) stringObjectMap.get("limit")),
                Sort.by(Sort.Direction.ASC, "createTime"));
        return departmentRepository.findAll(example, pageRequest);
    }

    @Override
    public void remove(String hoscode, String depcode) {

        Department department = departmentRepository.findByHoscodeAndDepcode(hoscode, depcode);
        if (department != null) {
            departmentRepository.deleteById(department.getId());
        }

    }

}
