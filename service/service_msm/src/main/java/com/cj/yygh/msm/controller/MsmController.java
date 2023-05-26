package com.cj.yygh.msm.controller;

import com.cj.yygh.msm.servie.MsmService;
import com.cj.yygh.result.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * MsmController
 * description:
 * 2023/5/21 21:28
 * Create by 杰瑞
 */
@RestController
@RequestMapping("/api/msm")
@Api(tags = "短信服务")
public class MsmController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private MsmService msmService;

    @PostMapping("/send/{phone}")
    public R sendCode(@PathVariable(value = "phone") String phone) {
        boolean flag = msmService.sendCode(phone);
        return flag ? R.ok() : R.error().message("验证码发送失败");
    }

}
