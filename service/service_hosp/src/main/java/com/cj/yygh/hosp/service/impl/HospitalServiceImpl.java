package com.cj.yygh.hosp.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cj.yygh.cmn.client.DictFeignClient;
import com.cj.yygh.constants.HospitalConstants;
import com.cj.yygh.enums.DictEnum;
import com.cj.yygh.hosp.repository.HospitalRepository;
import com.cj.yygh.hosp.service.HospitalService;
import com.cj.yygh.model.hosp.Hospital;
import com.cj.yygh.vo.hosp.HospitalQueryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

    @Autowired
    private DictFeignClient dictFeignClient;


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

    //做带条件查询的分页
    @Override
    public Page<Hospital> selectPage(Integer page, Integer limit, HospitalQueryVo hospitalQueryVo) {

        PageRequest pageRequest = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.ASC, "createTime"));
        Hospital hospital = new Hospital();

        BeanUtils.copyProperties(hospitalQueryVo, hospital);
        //创建匹配器，即如何使用查询条件
        ExampleMatcher matcher = ExampleMatcher.matching() //构建对象
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //改变默认字符串匹配方式；模糊查询
                .withIgnoreCase(true); //改变默认大小写忽略方式：忽略大小写
        Example<Hospital> example = Example.of(hospital, matcher);
        Page<Hospital> all = hospitalRepository.findAll(example, pageRequest);
        //获取里面的对象
        all.getContent().stream().forEach(item -> {
            this.packHospital(item);
        });


        return all;
    }

    @Override
    public void updateStatus(String id, Integer status) {
        if (status == 0 || status == 1) {
            Hospital hospital = hospitalRepository.findById(id).get();
            hospital.setStatus(status);
            hospital.setUpdateTime(new Date());
            hospitalRepository.save(hospital);
        }
    }

    @Override
    public Map<String, Object> getHospitalDetailById(String id) {
        Hospital hospital = hospitalRepository.findById(id).get();
        hospital = this.packHospital(hospital);
        Map<String, Object> map = new HashMap<>();
        map.put("hospital", hospital);
        map.put("bookingRule", hospital.getBookingRule());
        //不需要重复返回
        hospital.setBookingRule(null);
        return map;
    }

    @Override
    public List<Hospital> findByNameLike(String name) {

        return hospitalRepository.findByHosnameLike(name);
    }

    private Hospital packHospital(Hospital item) {
        String hospitalLevel = dictFeignClient.getName(DictEnum.HOSTYPE.getDictCode(), item.getHostype());

        String provinceName = dictFeignClient.getName(item.getProvinceCode());
        String cityName = dictFeignClient.getName(item.getCityCode());
        String districtName = dictFeignClient.getName(item.getDistrictCode());

        Map<String, Object> map = item.getParam();
        map.put("hostypeString", hospitalLevel);
        map.put("fullAddress", provinceName + cityName + districtName + item.getAddress());
        return item;
    }

}
