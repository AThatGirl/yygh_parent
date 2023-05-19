package com.cj.yygh.cmn.controller;


import com.cj.yygh.cmn.service.DictService;
import com.cj.yygh.model.cmn.Dict;
import com.cj.yygh.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <p>
 * 组织架构表 前端控制器
 * </p>
 *
 * @author cj
 * @since 2023-05-13
 */
@RestController
@RequestMapping("/admin/cmn/dict")
//@CrossOrigin
@Api("字典表")
public class DictController {

    @Autowired
    private DictService dictService;

    @ApiOperation(value = "根据dictCode获取下级节点")
    @GetMapping(value = "/findByDictCode/{dictCode}")
    public R findByDictCode(
            @ApiParam(name = "dictCode", value = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return R.ok().data("list",list);
    }


    @ApiOperation("根据父id查询所有子元素")
    @GetMapping("/childList/{pid}")
    public R getDictListByPid(@PathVariable Integer pid){
        List<Dict> dictList = dictService.getDictListByPid(pid);
        return R.ok().data("list", dictList);
    }

    @GetMapping("/exportData")
    @ApiOperation("导出excel")
    public void exportExcel(HttpServletResponse response) throws IOException {
        dictService.exportExcel(response);
    }

    @PostMapping("/importData")
    @ApiOperation("导入excel")
    public R importData(MultipartFile file) throws IOException {
        dictService.importData(file);
        return R.ok();
    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{parentDictCode}/{value}")
    public String getName(@ApiParam(name = "parentDictCode", value = "上级编码", required = true)
                                @PathVariable("parentDictCode") String parentDictCode,
                            @ApiParam(name = "value", value = "值", required = true)
                                @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue(parentDictCode, value);
    }

    @ApiOperation(value = "获取数据字典名称")
    @GetMapping(value = "/getName/{value}")
    public String getName(
            @ApiParam(name = "value", value = "值", required = true)
            @PathVariable("value") String value) {
        return dictService.getNameByParentDictCodeAndValue("", value);
    }




}

