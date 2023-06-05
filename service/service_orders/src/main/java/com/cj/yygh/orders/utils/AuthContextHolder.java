package com.cj.yygh.orders.utils;

import com.cj.yygh.utils.JwtHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * AuthContextHolder
 * description:
 * 2023/5/26 12:34
 * Create by 杰瑞
 */

public class AuthContextHolder {


    //根据请求报文中请求头中的token获取用户id
    public static Long getUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        return JwtHelper.getUserId(token);
    }

    //根据请求头中的token获取用户名
    public static String getUserName(HttpServletRequest request){
        String token = request.getHeader("token");
        return JwtHelper.getUserName(token);
    }

}
