package com.cj.yygh.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * ServiceOssApplication
 * description:
 * 2023/5/26 11:17
 * Create by 杰瑞
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(value = "com.cj")
public class ServiceOssApplication {

    public static void main(String[] args) {

        SpringApplication.run(ServiceOssApplication.class, args);

    }

}
