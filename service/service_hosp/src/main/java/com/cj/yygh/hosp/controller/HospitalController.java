package com.cj.yygh.hosp.controller;

import com.cj.yygh.hosp.service.HospitalService;
import com.cj.yygh.result.R;
import com.cj.yygh.vo.hosp.HospitalQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * HospitalController
 * description:
 * 2023/5/17 20:54
 * Create by 杰瑞
 */
@Api(tags = "医院接口")
@RestController
@RequestMapping("/admin/hosp/hospital")
@CrossOrigin
public class HospitalController {

    @Autowired
    private HospitalService hospitalService;

    @ApiOperation("根据医院id获取医院详情")
    @GetMapping("/detail/{id}")
    public R getHospitalDetailById(@PathVariable("id") String id) {
        Map<String, Object> map = hospitalService.getHospitalDetailById(id);
        return R.ok().data("hospital", map);
    }

    @ApiOperation("更新医院上线状态")
    @PutMapping("/updateStatus/{id}/{status}")
    public R updateStatus(@PathVariable("id") String id, @PathVariable("status") Integer status) {
        hospitalService.updateStatus(id, status);
        return R.ok();
    }


    @ApiOperation(value = "获取分页列表")
    @GetMapping("/{page}/{limit}")
    public R getHospitalPage(@PathVariable(value = "page") Integer page,
                             @PathVariable(value = "limit") Integer limit,
                             HospitalQueryVo hospitalQueryVo) {

        return R.ok().data("pages", hospitalService.selectPage(page, limit, hospitalQueryVo));
    }


}
