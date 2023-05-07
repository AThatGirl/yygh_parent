package com.cj.yygh.handler;

import com.cj.yygh.exception.YyghException;
import com.cj.yygh.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalExceptionHandler
 * description: 统一异常处理
 * 2023/5/6 21:23
 * Create by 杰瑞
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public R error(Exception ex) {
        ex.printStackTrace();
        log.error("全局异常", ex.getMessage());
        return R.error();
    }

    //特定异常处理
    @ExceptionHandler(ArithmeticException.class)
    public R error(ArithmeticException ex) {
        ex.printStackTrace();
        return R.error().message("出现数学异常");
    }

    //自定义异常处理，需要自己抛出
    @ExceptionHandler(YyghException.class)
    public R error(YyghException ex){
        ex.printStackTrace();
        return R.error().code(ex.getCode()).message(ex.getMessage());
    }


}
