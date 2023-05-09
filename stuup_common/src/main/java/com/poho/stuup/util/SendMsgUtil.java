package com.poho.stuup.util;

import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 09:24 2018/6/18
 * @Modified By:
 */
public class SendMsgUtil {
    private static final Logger logger = LoggerFactory.getLogger(SendMsgUtil.class);
    /**
     *
     * @param mobile
     * @return
     */
    public static boolean send(String mobile, String content) {
        boolean result = false;
      /*  String serverUrl = "http://api.msg.vip";
        String authToken = "e16fea998ea64e83b21cb2aa4af15cff";
        String appid = "ff808081638c600b0163dd38cc1a0005";
        String appSecret = "e1de6ec2e951403abbfc577e98a73cf2";
        String sign = "【医药学校】";
        String extcode = "100";
        try {
            SeerClientService clientService = new SeerClientService(serverUrl, authToken);
            String sendResult = clientService.sendSms(appid, appSecret, mobile, content, sign, extcode);
            JSONObject object = JSONObject.parseObject(sendResult);
            String code = object.getString("resultcode");
            String reportResult = clientService.getReport(appid, appSecret);
            logger.info("短信发送结果："+ reportResult);
            object = JSONObject.parseObject(reportResult);
            String reportCode = object.getString("resultcode");
            if ("Ex000000".equals(code) && "Ex000000".equals(reportCode)) {
                result = true;
            }
        } catch (ApiException e) {
            result = false;
        }*/
        return result;
    }

    /**
     * 发送确认验证码短信
     * @param mobile
     * @param msgCode
     * @return
     */
    public static boolean sendLoginCode(String mobile, String msgCode) {
        StringBuffer content = new StringBuffer();
        content.append("验证码：").append(msgCode);
        content.append("，您正在登录学生成长百草园系统，15分钟内有效，请尽快使用");
        return SendMsgUtil.send(mobile, content.toString());
    }

    /**
     * 发送确认验证码短信
     * @param mobile
     * @param msgCode
     * @return
     */
    public static boolean sendConfirmCode(String mobile, String msgCode) {
        StringBuffer content = new StringBuffer();
        content.append("验证码：").append(msgCode);
        content.append("，您正在进行医药学校监考报名，15分钟内有效，请妥善保管");
        return SendMsgUtil.send(mobile, content.toString());
    }

    /**
     * 发送考核开始通知短信
     * @param mobile
     * @param content
     * @return
     */
    public static boolean sendAssessStartMsg(String mobile, String content) {
        return SendMsgUtil.send(mobile, content);
    }
}
