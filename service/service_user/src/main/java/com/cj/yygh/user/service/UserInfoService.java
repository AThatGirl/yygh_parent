package com.cj.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.user.UserInfo;
import com.cj.yygh.vo.acl.UserQueryVo;
import com.cj.yygh.vo.user.LoginVo;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author cj
 * @since 2023-05-20
 */
public interface UserInfoService extends IService<UserInfo> {

    Map<String, Object> login(LoginVo loginVo);
}
