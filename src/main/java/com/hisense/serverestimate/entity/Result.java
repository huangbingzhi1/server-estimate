package com.hisense.serverestimate.entity;

import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;

/**
 * @author huangbingzhi
 * @version 1.0
 * @description
 * @date 2020/7/31 13:36
 */
public class Result<T> implements Serializable {
    private String code;
    private String message;
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code=code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message=message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data=data;
    }

    public static Result error(String message){
        Result result=new Result();
        result.setCode("500");
        result.setMessage(message);
        return result;
    }
    public static Result success(Object data){
        Result result=new Result();
        result.setCode("200");
        result.setData(data);
        return result;
    }
}
