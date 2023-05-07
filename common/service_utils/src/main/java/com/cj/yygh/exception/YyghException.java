package com.cj.yygh.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * YyghException
 * description:
 * 2023/5/6 21:36
 * Create by 杰瑞
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class YyghException extends RuntimeException {

    @ApiModelProperty(value = "状态码")
    private Integer  code;
    private String message;

}
