package com.poho.common.constant;

/**
 * 系统常量工具类
 *
 * @author ernest
 */
public class CommonConstants {
    /**
     * 成功
     */
    public static final int CODE_SUCCESS = 0;        //成功

    /**
     * 失败
     */
    public static final int CODE_FAIL = 1;

    /**
     * 失败
     */
    public static final int CODE_EXCEPTION = 1001;      //请求抛出异常
    public static final int CODE_NO_LOGIN = 1002;        //未登陆状态
    public static final int CODE_NO_AUTH = 1003;         //无操作权限
    public static final int CODE_LOGIN_EXPIRE = 1002;    //登录过期
    public static final int CODE_REFRESH_TOKEN = 1004;    //刷新token

    /**
     * token
     */
    public static final int JWT_CODE_EXPIRE = 1002;     //Token过期
    public static final int JWT_CODE_FAIL = 1006;       //验证不通过

    /**
     * jwt
     */
    public static final String JWT_ISS = "poho-jwt";//jwt签发者
    public static final String JWT_SECERT = "46cc793c53dc451b8a4fe2cd0bb00847";//密匙
    public static final long JWT_TTL = 7 * 24 * 60 * 60 * 1000;//token有效时间,单位毫秒
    public static final long JWT_TTL_COMMON = 30 * 60 * 1000;//token有效时间,单位毫秒
    public static final long JWT_TTL_ONE_HOUR = 60 * 60 * 1000;//token有效时间,单位毫秒
    public static final long JWT_TTL_ONE_DAY = 24 * 60 * 60 * 1000;//token有效时间,单位毫秒
    public static final long DURATION_MINUTE = 15L;//  15分钟

    public static final String CLAIMS_USER = "claimsUser";

    /**
     * 每页数据条数
     */
    public static final int PAGE_SIZE = 10;

    /**
     * 验证码
     */
    public static final String VERIFY_CODE_KEY = "verifyCode";

    public static final String EXCEL_XLS = ".xls";
    public static final String EXCEL_XLSX = ".xlsx";

    public static final int USER_FORBIDDEN_STATE = 2; //用户状态：禁用
}
