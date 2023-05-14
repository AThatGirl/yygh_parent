package com.cj.yygh.hosp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.yygh.hosp.md5.MD5;
import com.cj.yygh.hosp.service.HospitalSetService;
import com.cj.yygh.model.hosp.HospitalSet;
import com.cj.yygh.result.R;
import com.cj.yygh.vo.hosp.HospitalSetQueryVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

/**
 * <p>
 * 医院设置表 前端控制器
 * </p>
 *
 * @author cj
 * @since 2023-05-06
 */
@RestController
@RequestMapping("/admin/hosp/hospital-set")
@CrossOrigin
public class HospitalSetController {

    @Autowired
    private HospitalSetService hospitalSetService;

    @GetMapping("/findAll")
    @ApiOperation("查询所有医院")
    public R findAll() {
        List<HospitalSet> list = hospitalSetService.list();
        return R.ok().data("list", list);
    }

    @DeleteMapping("/remove/{id}")
    @ApiOperation("根据id删除医院")
    public R removeById(@ApiParam(name = "id", value = "医院id", required = true) @PathVariable("id") Integer id) {
        boolean b = hospitalSetService.removeById(id);
        return R.ok();
    }

    @ApiOperation("查询医院分页信息")
    @GetMapping("/{current}/{limit}")
    public R pageList(@PathVariable(value = "current") Integer current,
                      @PathVariable(value = "limit") Integer limit) {
        Page<HospitalSet> page = new Page<>(current, limit);
        hospitalSetService.page(page);
        return R.ok().data("total", page.getTotal()).data("rows", page.getRecords());
    }

    @GetMapping("/detail")
    public R getHospitalSetDetail(@RequestParam("id") Integer id) {
        return R.ok().data("item", hospitalSetService.getById(id));
    }

    @ApiOperation("更新医院信息")
    @PostMapping("/updateHospSet")
    public R updateHospitalSetDetail(@RequestBody HospitalSet hospitalSet) {
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

    @ApiOperation(value = "新增医院设置")
    @PostMapping("/saveHospSet")
    public R save(
            @ApiParam(name = "hospitalSet", value = "医院设置对象", required = true)
            @RequestBody HospitalSet hospitalSet) {
        //设置状态 1 使用 0 不能使用
        hospitalSet.setStatus(1);
        //签名秘钥
        Random random = new Random();
        hospitalSet.setSignKey(MD5.encrypt(System.currentTimeMillis() + "" + random.nextInt(1000)));
        hospitalSetService.save(hospitalSet);
        return R.ok();
    }

    @ApiOperation(value = "分页条件医院设置列表")
    @PostMapping("/{page}/{limit}")
    public R pageQuery(@ApiParam(name = "page", value = "当前页码", required = true)
                       @PathVariable Long page,
                       @ApiParam(name = "limit", value = "每页记录数", required = true)
                       @PathVariable Long limit,
                       @ApiParam(name = "hospitalSetQueryVo", value = "查询对象", required = false)
                       @RequestBody(required = false) HospitalSetQueryVo hospitalSetQueryVo) {
        Page<HospitalSet> pageParam = new Page<>(page, limit);
        QueryWrapper<HospitalSet> queryWrapper = new QueryWrapper<>();
        if (hospitalSetQueryVo != null) {
            String hosname = hospitalSetQueryVo.getHosname();
            String hoscode = hospitalSetQueryVo.getHoscode();
            if (!StringUtils.isEmpty(hosname)) {
                queryWrapper.like("hosname", hosname);
            }
            if (!StringUtils.isEmpty(hoscode)) {
                queryWrapper.eq("hoscode", hoscode);
            }
        }
        hospitalSetService.page(pageParam, queryWrapper);
        List<HospitalSet> records = pageParam.getRecords();
        long total = pageParam.getTotal();
        return R.ok().data("total", total).data("rows", records);
    }

    //批量删除医院设置
    @DeleteMapping("/batchRemove")
    public R batchRemoveHospitalSet(@RequestBody List<Long> idList) {
        hospitalSetService.removeByIds(idList);
        return R.ok();
    }

    // 医院设置锁定和解锁
    @ApiOperation("医院设置锁定和解锁")
    @PutMapping("/lockHospitalSet/{id}/{status}")
    public R lockHospitalSet(@PathVariable Long id,
                             @PathVariable Integer status) {
        //根据id查询医院设置信息
        HospitalSet hospitalSet = hospitalSetService.getById(id);
        //设置状态
        hospitalSet.setStatus(status);
        //调用方法
        hospitalSetService.updateById(hospitalSet);
        return R.ok();
    }

}

