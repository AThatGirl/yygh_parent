package com.cj.yygh.hosp.controller;

import com.cj.yygh.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserController
 * description:
 * 2023/5/7 20:55
 * Create by 杰瑞
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @PostMapping("/login")
    public R login(){
        return R.ok().data("token", "admin-token");
    }

    @GetMapping("/info")
    public R info(String token){
        log.info(token);
        return R.ok().data("roles","[admin]")
                .data("introduction", "superman")
                .data("avatar", "http://test.gif")
                .data("name","jerry");
    }

}
