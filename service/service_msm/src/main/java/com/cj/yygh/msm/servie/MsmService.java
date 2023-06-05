package com.cj.yygh.msm.servie;

import com.cj.yygh.vo.msm.MsmVo;

/**
 * MsmService
 * description:
 * 2023/5/21 21:30
 * Create by 杰瑞
 */
public interface MsmService {
    boolean sendCode(String phone);

    void send(MsmVo msmVo);
}
