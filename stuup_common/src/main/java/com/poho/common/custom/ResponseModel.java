package com.poho.common.custom;

import com.alibaba.fastjson.JSON;
import com.poho.common.constant.CommonConstants;

/**
 *  响应对象
 * @author wupeng
 */
public class ResponseModel<T> {
    /**
     * 响应码
     */
    private int code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 返回的数据
     */
    private T data;

    /**
     * token信息
     */
    private String token;

    public ResponseModel() {
    }

    public ResponseModel(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseModel(Integer code, T data, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseModel(Integer code, T data, String message, String token) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.token = token;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 请求返回数据处理
     *
     * @return
     */
    public String general() {
        return JSON.toJSONString(this);
    }

    public static <T> ResponseModel<T>  newSuccessData(T data){
        return new ResponseModel(CommonConstants.CODE_SUCCESS, data, "请求成功");
    }
}
