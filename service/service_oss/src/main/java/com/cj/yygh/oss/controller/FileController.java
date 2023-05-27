package com.cj.yygh.oss.controller;

import com.cj.yygh.oss.service.FileService;
import com.cj.yygh.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileController
 * description:
 * 2023/5/26 12:08
 * Create by 杰瑞
 */

@Api(tags = "文件上传")
@RestController
@RequestMapping("/admin/oss/file")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 文件上传
     */
    @ApiOperation(value = "文件上传")
    @PostMapping("/upload")
    public R upload(MultipartFile file) {
        String uploadUrl = fileService.uploadFile(file);
        return R.ok().message("文件上传成功").data("url", uploadUrl);
    }


}


