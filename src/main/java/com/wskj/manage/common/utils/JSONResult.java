package com.wskj.manage.common.utils;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public class JSONResult implements Serializable {

    private static final long serialVersionUID = 3147082039461940823L;

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    public static final int EXCEPTION = 2;

    private int code;

    private String msg;

    private Object data;

    public JSONResult(){}

    public JSONResult(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static JSONResult ok(){
        JSONResult res = new JSONResult();
        res.setCode(SUCCESS);
        res.setMsg("操作成功");
        return res;
    }

    public static JSONResult ok(String msg){
        JSONResult res = new JSONResult();
        res.setCode(SUCCESS);
        res.setMsg(msg);
        return res;
    }

    public static JSONResult ok(Object data){
        JSONResult res = new JSONResult();
        res.setCode(SUCCESS);
        res.setData(data);
        return res;
    }

    public static JSONResult ok(String msg,Object data){
        JSONResult res = ok(msg);
        res.setData(data);
        return res;
    }

    public static JSONResult fail(String msg){
        JSONResult res = new JSONResult();
        res.setCode(FAIL);
        res.setMsg(msg);
        return res;
    }

    public static JSONResult fail(String msg,Object data){
        JSONResult res = fail(msg);
        res.setData(data);
        return res;
    }

    public static JSONResult ex(Throwable e){
        JSONResult res = new JSONResult();
        res.setCode(EXCEPTION);
        res.setMsg(e.getMessage());
        return res;
    }

    public static JSONResult ex(String msg){
        JSONResult res = new JSONResult();
        res.setCode(EXCEPTION);
        res.setMsg(msg);
        return res;
    }

    public static JSONResult ex(String msg, Object data){
        JSONResult res = ex(msg);
        res.setData(data);
        return res;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
