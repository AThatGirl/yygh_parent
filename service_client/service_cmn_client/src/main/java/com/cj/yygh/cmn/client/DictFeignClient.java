package com.cj.yygh.cmn.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * DictFeignClient
 * description:
 * 2023/5/18 10:13
 * Create by 杰瑞
 */
@FeignClient("service-cmn")
public interface DictFeignClient {

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/admin/cmn/dict/getName/{parentDictCode}/{value}")
    String getName(@PathVariable("parentDictCode") String parentDictCode, @PathVariable("value") String value);

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/admin/cmn/dict/getName/{value}")
    String getName(@PathVariable("value") String value);



}
