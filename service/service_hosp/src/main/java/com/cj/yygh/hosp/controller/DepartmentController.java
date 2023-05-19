package com.cj.yygh.hosp.controller;

import com.cj.yygh.hosp.service.DepartmentService;
import com.cj.yygh.result.R;
import com.cj.yygh.vo.hosp.DepartmentVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DepartmentController
 * description:
 * 2023/5/18 20:06
 * Create by 杰瑞
 */

@RestController
@RequestMapping("/admin/hosp/department")
@Api(tags = "部门")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;


    @ApiOperation("根据医院编号查询该医院下的科室信息")
    @GetMapping("/getDeptList/{hoscode}")
    public R getAllDepts(@PathVariable(value = "hoscode") String hoscode){

        List<DepartmentVo> departmentVoList = departmentService.getAllDepts(hoscode);
        return R.ok().data("list", departmentVoList);
    }


}
