package com.poho.common.custom;

import com.poho.common.constant.CommonConstants;
import com.poho.common.util.JwtUtil;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 00:26 2018/6/10
 * @Modified By:
 */
public class ResponseMsg {
    /**
     * 成功请求不带数据
     *
     * @return
     */
    public static String success() {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_SUCCESS, "success");
        return model.general();
    }

    /**
     * 成功请求带返回数据
     *
     * @param data
     * @return
     */
    public static String successWithData(Object data) {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_SUCCESS, data, "success");
        return model.general();
    }

    /**
     * 服务器异常不带数据
     *
     * @return
     */
    public static String error() {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_EXCEPTION, "请稍后再试");
        return model.general();
    }

    /**
     * 服务器异常带数据
     *
     * @param data
     * @return
     */
    public static String errorWithData(Object data) {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_EXCEPTION, data, "请稍后再试");
        return model.general();
    }

    /**
     * 服务器异常带数据和消息
     *
     * @param data
     * @return
     */
    public static String errorWithData(String msg, Object data) {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_EXCEPTION, data, msg);
        return model.general();
    }

    /**
     * 用户未登录
     *
     * @return
     */
    public static String noLogin() {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_NO_LOGIN, "用户未登录，请登录");
        return model.general();
    }


    /**
     * 无操作权限
     *
     * @return
     */
    public static String noAuth() {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_NO_AUTH, "拒绝授权");
        return model.general();
    }

    /**
     * 登录过期
     *
     * @return
     */
    public static String loginExpire() {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_LOGIN_EXPIRE, "登录过期，请重新登录");
        return model.general();
    }

    /**
     * 刷新token
     *
     * @return
     */
    public static String refreshToken(String userId) {
        ResponseModel model = new ResponseModel(CommonConstants.CODE_REFRESH_TOKEN, "刷新token");
        model.setToken(JwtUtil.createJwt(userId, CommonConstants.JWT_TTL_COMMON));
        return model.general();
    }
}
