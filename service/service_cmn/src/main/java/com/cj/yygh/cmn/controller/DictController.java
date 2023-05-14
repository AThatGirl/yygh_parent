package com.cj.yygh.cmn.controller;


import com.cj.yygh.cmn.service.DictService;
import com.cj.yygh.model.cmn.Dict;
import com.cj.yygh.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@CrossOrigin
@Api("字典表")
public class DictController {

    @Autowired
    private DictService dictService;


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



}
