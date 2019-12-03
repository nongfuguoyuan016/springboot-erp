package com.wskj.manage.common.enums;

/**
 * @author: xuchang
 * @date: 2019/10/28
 */
public enum ResultCodeEnum {

    SUCCESS(0,"操作成功"),FAIL(1,"操作失败"),EXCEPTION(2,"操作异常,请稍后再试"),
    NOT_LOGIN(50001,"未登录,请重新登录"),INVALID_REQUEST(50002,"非法请求");

    private int code;

    private String msg;

    ResultCodeEnum(int code,String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
