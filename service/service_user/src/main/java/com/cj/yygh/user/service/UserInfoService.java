package com.cj.yygh.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cj.yygh.model.user.UserInfo;
import com.cj.yygh.vo.acl.UserQueryVo;
import com.cj.yygh.vo.user.LoginVo;
import com.cj.yygh.vo.user.UserAuthVo;

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

    UserInfo selectByOpenId(String openid);

    UserInfo selectById(Long userId);

    void saveUserAuth(Long userId, UserAuthVo userAuthVo);

}
