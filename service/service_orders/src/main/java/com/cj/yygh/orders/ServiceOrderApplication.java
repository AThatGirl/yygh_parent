package com.cj.yygh.orders;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * ServiceOrderApplication
 * description:
 * 2023/5/29 11:10
 * Create by 杰瑞
 */


@SpringBootApplication
@MapperScan("com.cj.yygh.orders.mapper")
@ComponentScan(basePackages = {"com.cj"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.cj"})
public class ServiceOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceOrderApplication.class, args);
    }
}