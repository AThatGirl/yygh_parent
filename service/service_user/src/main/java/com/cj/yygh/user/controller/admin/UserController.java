package com.cj.yygh.user.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cj.yygh.model.user.UserInfo;
import com.cj.yygh.result.R;
import com.cj.yygh.user.service.UserInfoService;
import com.cj.yygh.vo.user.UserInfoQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * UserController
 * description:
 * 2023/5/27 16:28
 * Create by 杰瑞
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/userinfo")
public class UserController {

    @Autowired
    private UserInfoService userInfoService;

    @ApiOperation("认证审批")
    @GetMapping("/approval/{userId}/{authStatus}")
    public R approval(@PathVariable Long userId,@PathVariable Integer authStatus) {
        userInfoService.approval(userId,authStatus);
        return R.ok();
    }


    @ApiOperation("用户详情")
    @GetMapping("/show/{userId}")
    public R show(@PathVariable Long userId) {
        Map<String,Object> map = userInfoService.show(userId);
        return R.ok().data(map);
    }

    @ApiOperation(value = "锁定")
    @GetMapping("lock/{userId}/{status}")
    public R lock(
            @PathVariable("userId") Long userId,
            @PathVariable("status") Integer status){
        userInfoService.lock(userId, status);
        return R.ok();
    }

    @ApiOperation("条件查询带分页")
    @GetMapping("/{page}/{limit}")
    public R getUserInfoPage(@PathVariable("page") Integer page,
                             @PathVariable("limit") Integer limit,
                             UserInfoQueryVo userInfoQueryVo){
        Page<UserInfo> pageModel = userInfoService.selectUserInfoPage(page, limit, userInfoQueryVo);
        return R.ok().data("pageModel", pageModel);

    }

}
