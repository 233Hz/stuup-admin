package com.poho.stuup.custom;

import io.swagger.annotations.ApiModelProperty;

/**
 * 自定义短信发送结果
 * @author wupeng
 */
public class CusMsg {
    @ApiModelProperty("验证码ID")
    private Long msgId;
    private String msgCode;

    public CusMsg(Long msgId) {
        this.msgId = msgId;
    }

    public CusMsg(Long msgId, String msgCode) {
        this.msgId = msgId;
        this.msgCode = msgCode;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }
}
