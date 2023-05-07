package com.cj.yygh.result;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * R
 * description:
 * 2023/5/6 19:40
 * Create by 杰瑞
 */
@Data
public class R {

    private Integer code;

    private String message;

    private boolean success;

    private Map<String, Object> data = new HashMap<>();

    private R(){}

    public static R ok(){
        R r = new R();
        r.setCode(ResultCode.SUCCESS_CODE);
        r.setMessage(ResultCode.SUCCESS_MESSAGE);
        r.setSuccess(true);
        return r;
    }

    public static R error(){
        R r = new R();
        r.setCode(ResultCode.ERROR_CODE);
        r.setMessage(ResultCode.ERROR_MESSAGE);
        r.setSuccess(false);
        return r;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R success(boolean success){
        this.setSuccess(success);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }


    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

}
