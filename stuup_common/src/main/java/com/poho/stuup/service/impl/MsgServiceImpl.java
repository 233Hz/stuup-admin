package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.common.util.Validator;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusMsg;
import com.poho.stuup.dao.ConfigMapper;
import com.poho.stuup.dao.MsgMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.Config;
import com.poho.stuup.model.Msg;
import com.poho.stuup.model.User;
import com.poho.stuup.service.IMsgService;
import com.poho.stuup.util.SendMsgUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 13:56 2019/12/1
 * @Modified By:
 */
@Service
public class MsgServiceImpl implements IMsgService {
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ConfigMapper configMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return msgMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Msg record) {
        return msgMapper.insert(record);
    }

    @Override
    public int insertSelective(Msg record) {
        return msgMapper.insertSelective(record);
    }

    @Override
    public Msg selectByPrimaryKey(Long oid) {
        return msgMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Msg record) {
        return msgMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Msg record) {
        return msgMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel<CusMsg> sendLoginCode(String mobile) {
        ResponseModel model = new ResponseModel();
        if (Validator.isMobile(mobile)) {
            Map<String, Object> map = new HashMap<>();
            map.put("mobile", mobile);
            User user = userMapper.checkUser(map);
            if (MicrovanUtil.isNotEmpty(user)) {
                if (user.getState().intValue() == ProjectConstants.USER_STATE_COMMON) {
                    String code = MicrovanUtil.getRandomNumChar(4);
                    String validateCode = "true";
                    Config config = configMapper.selectByPrimaryKey(ProjectConstants.PARAM_VALIDATE_CODE);
                    if (config != null && MicrovanUtil.isNotEmpty(config.getConfigValue())) {
                        validateCode = config.getConfigValue();
                    }
                    boolean sendResult = false;
                    if ("false".equals(validateCode)) {
                        sendResult = true;
                    } else {
                        sendResult = SendMsgUtil.sendLoginCode(mobile, code);
                    }
                    if (sendResult) {
                        Msg msg = new Msg();
                        msg.setSendTime(new Date());
                        msg.setMobile(mobile);
                        msg.setMsgCode(code);
                        msg.setMsgType(ProjectConstants.MSG_TYPE_CODE);
                        msg.setExpireTime(msg.getSendTime().getTime() + ProjectConstants.MSG_VALID_TIME);
                        msgMapper.insertSelective(msg);
                        model.setCode(CommonConstants.CODE_SUCCESS);
                        model.setMessage("验证码已发送至您的手机" + MicrovanUtil.replacePhoneStar(mobile));
                        if ("false".equals(validateCode)) {
                            model.setData(new CusMsg(msg.getOid(), code));
                        } else {
                            model.setData(new CusMsg(msg.getOid()));
                        }
                    } else {
                        model.setCode(CommonConstants.CODE_EXCEPTION);
                        model.setMessage("发送失败，请稍后重试");
                    }
                } else {
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                    model.setMessage("你的账号已被禁止登录系统");
                }
            } else {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMessage("你还不是本系统用户");
            }
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("手机号码错误");
        }
        return model;
    }
}
