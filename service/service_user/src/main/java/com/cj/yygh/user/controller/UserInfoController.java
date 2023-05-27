package com.cj.yygh.user.controller;


import com.cj.yygh.model.user.UserInfo;
import com.cj.yygh.result.R;
import com.cj.yygh.user.service.UserInfoService;
import com.cj.yygh.user.utils.AuthContextHolder;
import com.cj.yygh.vo.user.LoginVo;
import com.cj.yygh.vo.user.UserAuthVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author cj
 * @since 2023-05-20
 */
@Api(tags = "用户接口")
@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;


    @PostMapping("/auth/userAuth")
    public R saveUserAuth(@RequestBody UserAuthVo userAuthVo, HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);
        userInfoService.saveUserAuth(userId, userAuthVo);
        return R.ok();
    }


    @ApiOperation("获取用户id信息接口")
    @RequestMapping("/auth/getUserInfo")
    public R getUserInfo(HttpServletRequest request) {
        Long userId = AuthContextHolder.getUserId(request);

        UserInfo userInfo = userInfoService.selectById(userId);
        return R.ok().data("userInfo", userInfo);
    }

    @ApiOperation("会员登录")
    @PostMapping("/login")
    public R login(@RequestBody LoginVo loginVo) {
        Map<String, Object> map = userInfoService.login(loginVo);
        return R.ok().data(map);

    }

}

