package com.hz.usercenterbackend.common;

import lombok.Data;

import java.io.Serializable;

public class ResultUtils {

    public static <T> BaseResponse<T> success(T data){
        return new BaseResponse<>(0, "OK", data);
    }

    public static  BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    public static  BaseResponse error(int code,String message,String description){
        return new BaseResponse(code, message,null, description);
    }

    public static  BaseResponse error(ErrorCode errorCode,String message,String description){
        return new BaseResponse(errorCode.getCode(), message,null, description);
    }

    public static  BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode.getCode(),errorCode.getMessage(),null,description);
    }
}
