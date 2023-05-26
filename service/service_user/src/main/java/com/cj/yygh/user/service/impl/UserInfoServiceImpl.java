package com.cj.yygh.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cj.yygh.constants.UserConstants;
import com.cj.yygh.exception.YyghException;
import com.cj.yygh.model.user.UserInfo;
import com.cj.yygh.user.mapper.UserInfoMapper;
import com.cj.yygh.user.service.UserInfoService;
import com.cj.yygh.utils.JwtHelper;
import com.cj.yygh.vo.user.LoginVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author cj
 * @since 2023-05-20
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {


    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public Map<String, Object> login(LoginVo loginVo) {

        //获取用户输入的手机号和验证码
        String phone = loginVo.getPhone();
        String code = loginVo.getCode();
        //要对用户输入的手机号和验证码进行非空校验
        if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(code)) {
            throw new YyghException(20001, "用户名或验证码不能为空");
        }
        String redisCode = redisTemplate.opsForValue().get(phone);
        if (!code.equals(redisCode)) {
            throw new YyghException(20001, "验证码错误");
        }

        //判断是不是首次登录，如果是，完成自动注册
        UserInfo userInfo = baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("phone", phone));
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setPhone(phone);
            userInfo.setStatus(UserConstants.STATUS_NORMAL);
            userInfo.setCreateTime(new Date());
            baseMapper.insert(userInfo);
        } else {

            UserInfo userInfoFinal = new UserInfo();
            UserInfo u1 = baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("phone", phone));
            if (u1 != null) {
                BeanUtils.copyProperties(u1, userInfoFinal);
                baseMapper.delete(new QueryWrapper<UserInfo>().eq("phone", phone));
            }else {
                userInfoFinal.setPhone(phone);
            }

            userInfo = baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("openid", loginVo.getOpenid()));
            userInfoFinal.setId(userInfo.getId());
            userInfoFinal.setNickName(userInfoFinal.getNickName());
            userInfoFinal.setOpenid(userInfo.getOpenid());
            userInfoFinal.setStatus(userInfo.getStatus());
            baseMapper.updateById(userInfo);
        }


        //判断用户状态
        if (userInfo.getStatus().equals(UserConstants.STATUS_CLOCKING)) {
            throw new YyghException(20001, "该用户已经被禁用");
        }
        //返回用户信息
        Map<String, Object> map = new HashMap<>();

        String name = userInfo.getName();
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getNickName();
        }
        if (StringUtils.isEmpty(name)) {
            name = userInfo.getPhone();
        }

        map.put("name", name);
        String token = JwtHelper.createToken(userInfo.getId(), userInfo.getName());
        map.put("token", token);
        return map;
    }

    @Override
    public UserInfo selectByOpenId(String openid) {
        return baseMapper.selectOne(new QueryWrapper<UserInfo>().eq("openid", openid));
    }
}
