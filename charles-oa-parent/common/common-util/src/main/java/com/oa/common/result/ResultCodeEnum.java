package com.oa.common.result;

import lombok.Getter;

/**
 * @author Charles
 * @create 2023-04-02-22:56
 */

@Getter
public enum ResultCodeEnum {

    SUCCESS(200,"成功"),
    FAIL(201,"失败"),
    LOGIN_ERROR(208,"登录失败");


    private Integer code;
    private String message;

    ResultCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
