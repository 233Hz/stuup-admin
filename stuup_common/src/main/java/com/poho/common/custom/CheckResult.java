package com.poho.common.custom;

import io.jsonwebtoken.Claims;

/**
 * JWT验证结果模型
 */
public class CheckResult {
    private int code;
    private boolean success;
    private Claims claims;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Claims getClaims() {
        return claims;
    }

    public void setClaims(Claims claims) {
        this.claims = claims;
    }
}