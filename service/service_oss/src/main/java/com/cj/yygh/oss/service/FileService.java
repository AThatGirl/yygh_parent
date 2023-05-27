package com.cj.yygh.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * FileService
 * description:
 * 2023/5/26 11:26
 * Create by 杰瑞
 */
public interface FileService {

    String uploadFile(MultipartFile file);

}
