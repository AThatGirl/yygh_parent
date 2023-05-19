package com.cj.yygh.hosp.controller;

import com.cj.yygh.result.R;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 * description:
 * 2023/5/7 20:55
 * Create by 杰瑞
 */
@RestController
@RequestMapping("/yygh/user")
//@CrossOrigin
@Slf4j
@Api(tags = "用户")
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
