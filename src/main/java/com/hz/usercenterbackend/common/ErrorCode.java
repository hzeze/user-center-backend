package com.hz.usercenterbackend.common;

public enum ErrorCode {

    PARAMS_ERROR(40000,"请求参数错误",""),
    NULL_ERROR(40001,"请求参数为空",""),
    NOT_LOGIN(40100,"未登录",""),
    NO_AUTH(40101,"无权限",""),
    SUCCESS(0,"OK",""),
    SYSTEM_ERROR(50000,"系统内部异常","");


    private final int code;
    private final String message;
    private final String description;

    ErrorCode(int code,String message , String description ) {
        this.description = description;
        this.message = message;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
