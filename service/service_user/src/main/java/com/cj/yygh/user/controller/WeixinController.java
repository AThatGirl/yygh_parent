package com.cj.yygh.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.cj.yygh.constants.UserConstants;
import com.cj.yygh.model.user.UserInfo;
import com.cj.yygh.result.R;
import com.cj.yygh.user.service.UserInfoService;
import com.cj.yygh.user.utils.ConstantPropertiesUtil;
import com.cj.yygh.user.utils.HttpClientUtils;
import com.cj.yygh.utils.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * WeixinController
 * description:
 * 2023/5/22 16:16
 * Create by 杰瑞
 */
@Controller
@RequestMapping("/api/ucenter/wx")
public class WeixinController {


    @Autowired
    private UserInfoService userInfoService;


    @GetMapping("/callback")
    public String callback(String code, String state) {

        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");
        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantPropertiesUtil.WX_OPEN_APP_ID,
                ConstantPropertiesUtil.WX_OPEN_APP_SECRET,
                code);
        //httpClient请求地址
        try {
            String jsonStr = HttpClientUtils.get(accessTokenUrl);
            JSONObject jsonObject = JSONObject.parseObject(jsonStr);
            String accessToken = jsonObject.getString("accessToken");
            String openid = jsonObject.getString("openid");

            UserInfo userInfo = userInfoService.selectByOpenId(openid);

            if (userInfo == null) {
                //说明微信首次登录
                userInfo = new UserInfo();
                StringBuffer append = new StringBuffer()
                        .append("https://api.weixin.qq.com/sns/oauth2/access_token" + accessToken)
                        .append("&openid=" + openid);

                String userInfoStr = HttpClientUtils.get(append.toString());
                String nickname = JSONObject.parseObject(userInfoStr).getString("nickname");

                userInfo.setOpenid(openid);
                userInfo.setNickName(nickname);
                userInfo.setStatus(UserConstants.STATUS_NORMAL);
                userInfoService.save(userInfo);

            }
            //给前端返回用户信息：name，token
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

            //多返回openid
            //判断更根据openid查询出来的手机号是否为空，说明微信和手机号没有绑定过，返回openid
            //前端拿到不为空的openid，需要做手机号的绑定
            if (StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", openid);
            } else {
                map.put("openid", "");
            }
            //跳转到前端页面
            return "redirect:http://localhost:3000/weixin/callback?token=" + map.get("token") + "&openid=" + map.get("openid") + "&name=" + URLEncoder.encode((String) map.get("name"), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取微信登录参数
     */
    @GetMapping("getLoginParam")
    @ResponseBody
    public R genQrConnect() throws UnsupportedEncodingException {
        String redirectUri = URLEncoder.encode(ConstantPropertiesUtil.WX_OPEN_REDIRECT_URL, "UTF-8");
        Map<String, Object> map = new HashMap<>();
        map.put("appid", ConstantPropertiesUtil.WX_OPEN_APP_ID);
        map.put("redirectUri", redirectUri);
        map.put("scope", "snsapi_login");
        map.put("state", System.currentTimeMillis() + "");//System.currentTimeMillis()+""
        return R.ok().data(map);
    }


}
