package com.poho.common.custom;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.alibaba.fastjson.JSON;
import com.poho.common.constant.CommonConstants;

/**
 * 响应对象
 *
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
    private String msg;

    /**
     * 返回的数据
     */
    private T data;

    /**
     * sa-token
     */
    private SaTokenInfo tokenInfo;

    public ResponseModel() {
    }

    public ResponseModel(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseModel(Integer code, T data, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseModel<T> ok() {
        return restResult(null, CommonConstants.CODE_SUCCESS, null);
    }

    public static <T> ResponseModel<T> ok(T data) {
        return restResult(data, CommonConstants.CODE_SUCCESS, null);
    }

    public static <T> ResponseModel<T> ok(T data, String msg) {
        return restResult(data, CommonConstants.CODE_SUCCESS, msg);
    }

    public static <T> ResponseModel<T> failed() {
        return restResult(null, CommonConstants.CODE_FAIL, null);
    }

    public static <T> ResponseModel<T> failed(String msg) {
        return restResult(null, CommonConstants.CODE_FAIL, msg);
    }

    public static <T> ResponseModel<T> failed(T data) {
        return restResult(data, CommonConstants.CODE_FAIL, null);
    }

    public static <T> ResponseModel<T> failed(T data, String msg) {
        return restResult(data, CommonConstants.CODE_FAIL, msg);
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

    public void setMsg(String message) {
        this.msg = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public SaTokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(SaTokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public String general() {
        return JSON.toJSONString(this);
    }

    public static <T> ResponseModel<T> newSuccessData(T data) {
        return new ResponseModel<T>(CommonConstants.CODE_SUCCESS, data, "请求成功");
    }

    private static <T> ResponseModel<T> restResult(T data, int code, String msg) {
        ResponseModel<T> apiResult = new ResponseModel<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
