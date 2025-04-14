package com.hz.usercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用返回类
 * @param <T>
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int code;

    private String msg;

    private T data;

    private String description;

    public BaseResponse(int code, String msg, T data, String description) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.description = description;
    }

    public BaseResponse(int code, String msg) {
        this(code, msg, null, null);
    }

    public BaseResponse(int code, String msg, T data) {
        this(code, msg, data, null);
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(), errorCode.getMessage(), null,errorCode.getDescription()) ;
    }

}
