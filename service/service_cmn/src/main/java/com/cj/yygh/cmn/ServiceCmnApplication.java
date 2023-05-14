package com.cj.yygh.cmn;

/**
 * ServiceCmnApplication
 * description:
 * 2023/5/13 21:21
 * Create by 杰瑞
 */

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.cj"})//swagger
@MapperScan(basePackages = "com.cj.yygh.cmn.mapper")
public class ServiceCmnApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceCmnApplication.class, args);
    }
}