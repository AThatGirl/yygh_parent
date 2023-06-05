package com.cj.yygh.msm.servie.impl;

import com.cj.yygh.msm.servie.MsmService;
import com.cj.yygh.msm.utils.HttpUtils;
import com.cj.yygh.msm.utils.RandomUtil;
import com.cj.yygh.vo.msm.MsmVo;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * MsmServiceImpl
 * description:
 * 2023/5/21 21:30
 * Create by 杰瑞
 */
@Service
public class MsmServiceImpl implements MsmService {



    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean sendCode(String phone) {

        //先看redis是否有验证码
        String s = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(s)) {
            return true;
        }

        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsSend";
        String method = "POST";
        String appcode = "21ef4f54fca84c76aba6c1a2973e3d6b";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phone);
        String code = RandomUtil.getFourBitRandom();
        querys.put("param", "**code**:" + code + ",**minute**:5");

//smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html

        querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
        Map<String, String> bodys = new HashMap<String, String>();


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从\r\n\t    \t* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java\r\n\t    \t* 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
            //保存到redis
            redisTemplate.opsForValue().set(phone, code, 1, TimeUnit.HOURS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void send(MsmVo msmVo) {
        String phone = msmVo.getPhone();
//        this.sendCode(phone);
        System.out.println("发送短信");

    }
}
