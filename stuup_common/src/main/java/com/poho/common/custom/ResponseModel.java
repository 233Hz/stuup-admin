package com.poho.common.custom;

import cn.dev33.satoken.stp.SaTokenInfo;
import com.alibaba.fastjson.JSON;
import com.poho.common.constant.CommonConstants;

import java.util.List;

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
    private String msg;

    /**
     * 返回的数据
     */
    private T data;

    /**
     * token信息
     */
    private String token;

    /**
     * sa-token
     */
    private SaTokenInfo tokenInfo;

    private List<String> roleCodeList;
    private List<String> permissionList;

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

    public ResponseModel(Integer code, T data, String message, String token) {
        this.code = code;
        this.msg = message;
        this.data = data;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SaTokenInfo getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(SaTokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;
    }

    public List<String> getRoleCodeList() {
        return roleCodeList;
    }

    public void setRoleCodeList(List<String> roleCodeList) {
        this.roleCodeList = roleCodeList;
    }

    public List<String> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<String> permissionList) {
        this.permissionList = permissionList;
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

    private static <T> ResponseModel<T> restResult(T data, int code, String msg) {
        ResponseModel<T> apiResult = new ResponseModel<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        return apiResult;
    }
}
