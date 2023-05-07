package com.cj.yygh.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * PaginationConfig
 * description:
 * 2023/5/6 19:57
 * Create by 杰瑞
 */
@Configuration
public class PaginationConfig {

    /**
     * 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
