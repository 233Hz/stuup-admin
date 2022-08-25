package com.poho.stuup.service.impl;

import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.service.ITaskService;
import com.poho.stuup.util.SendMsgUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 15:21 2020/10/21
 * @Modified By:
 */
@Service
public class TaskServiceImpl implements ITaskService {
    @Resource
    private YearMapper yearMapper;
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private AssessRangeMapper assessRangeMapper;
    @Resource
    private AssessRecordMapper assessRecordMapper;

    @Override
    public void remindStart() {
        Year year = yearMapper.findCurrYear();
        long now = System.currentTimeMillis();
        if (now > year.getYearStart().getTime() && now < year.getYearEnd().getTime()) {
            Map<String, Object> param = new HashMap<>();
            param.put("yearId", year.getOid());
            List<User> userList = userMapper.findRangeUserList(param);
            if (MicrovanUtil.isNotEmpty(userList)) {
                for (User user : userList) {
                    param.put("userId", user.getOid());
                    param.put("msgType", ProjectConstants.MSG_TYPE_START);
                    Msg msg = msgMapper.checkYearMsg(param);
                    if (MicrovanUtil.isEmpty(msg) && MicrovanUtil.isNotEmpty(user.getMobile())) {
                        StringBuffer content = new StringBuffer();
                        content.append(user.getUserName()).append("老师，");
                        content.append(year.getYearName()).append("绩效考核打分已开始，请及时处理考核打分任务。");
                        boolean sendResult = SendMsgUtil.sendAssessStartMsg(user.getMobile(), content.toString());
                        if (sendResult) {
                            msg = new Msg();
                            msg.setSendTime(new Date());
                            msg.setMobile(user.getMobile());
                            msg.setMsgType(ProjectConstants.MSG_TYPE_START);
                            msg.setYearId(year.getOid());
                            msg.setUserId(user.getOid());
                            msg.setMsgCode(content.toString());
                            msgMapper.insertSelective(msg);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void remindEnd() {
        Year year = yearMapper.findCurrYear();
        long now = System.currentTimeMillis();
        if (now > year.getYearStart().getTime() && now < year.getYearEnd().getTime()) {
            Date before = MicrovanUtil.formatDateYMD(new Date());
            Date after = MicrovanUtil.formatDateYMD(year.getYearEnd());
            int day = MicrovanUtil.dayOfTwoDate(before, after);
            if (day == 1 || day == 3) {
                Map<String, Object> param = new HashMap<>();
                param.put("yearId", year.getOid());
                List<User> userList = userMapper.findRangeUserList(param);
                if (MicrovanUtil.isNotEmpty(userList)) {
                    for (User user : userList) {
                        param.put("userId", user.getOid());
                        List<Long> roleIds = userRoleMapper.queryUserYearRoles(param);
                        StringBuffer stringBuffer = new StringBuffer();
                        if (roleIds.contains(ProjectConstants.ROLE_DZLD)) {
                            param.put("assessUser", user.getOid());
                            //党政领导评中层
                            param.put("assessType", ProjectConstants.ASSESS_TYPE_DZLDPZC);
                            int recordTotal = assessRecordMapper.queryTotal(param);
                            if (recordTotal == 0) {
                                stringBuffer.append("党政领导班子评中层，");
                            }
                            AssessRange assessRange = assessRangeMapper.checkAssessRange(param);
                            if (MicrovanUtil.isNotEmpty(assessRange) && MicrovanUtil.isNotEmpty(assessRange.getDeptId())) {
                                //分管领导评中层
                                param.put("assessType", ProjectConstants.ASSESS_TYPE_FGLDPZC);
                                recordTotal = assessRecordMapper.queryTotal(param);
                                if (recordTotal == 0) {
                                    stringBuffer.append("分管领导评中层，");
                                }
                            }
                        }
                        if (roleIds.contains(ProjectConstants.ROLE_FGLD)) {
                            param.put("assessUser", user.getOid());
                            //党政领导评中层
                            param.put("assessType", ProjectConstants.ASSESS_TYPE_DZLDPZC);
                            int recordTotal = assessRecordMapper.queryTotal(param);
                            if (recordTotal == 0) {
                                stringBuffer.append("党政领导班子评中层，");
                            }
                            AssessRange assessRange = assessRangeMapper.checkAssessRange(param);
                            if (MicrovanUtil.isNotEmpty(assessRange) && MicrovanUtil.isNotEmpty(assessRange.getDeptId())) {
                                //分管领导评中层
                                param.put("assessType", ProjectConstants.ASSESS_TYPE_FGLDPZC);
                                recordTotal = assessRecordMapper.queryTotal(param);
                                if (recordTotal == 0) {
                                    stringBuffer.append("分管领导评中层，");
                                }
                            }
                        }
                        if (roleIds.contains(ProjectConstants.ROLE_BMFZR) || roleIds.contains(ProjectConstants.ROLE_QTZC)) {
                            //自评互评
                            param.put("assessType", ProjectConstants.ASSESS_TYPE_FGLDPZC);
                            int recordTotal = assessRecordMapper.queryTotal(param);
                            if (recordTotal == 0) {
                                stringBuffer.append("中层自评互评，");
                            }
                            //评员工
                            param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCPYG);
                            recordTotal = assessRecordMapper.queryTotal(param);
                            if (recordTotal == 0) {
                                stringBuffer.append("中层评员工，");
                            }
                        }
                        if (roleIds.contains(ProjectConstants.ROLE_PTJS)) {
                            //评中层
                            param.put("assessType", ProjectConstants.ASSESS_TYPE_QZPZC);
                            int recordTotal = assessRecordMapper.queryTotal(param);
                            if (recordTotal == 0) {
                                stringBuffer.append("群众测评，");
                            }
                        }
                        if (MicrovanUtil.isNotEmpty(stringBuffer.toString())) {
                            StringBuffer content = new StringBuffer();
                            content.append(user.getUserName()).append("老师，");
                            content.append(year.getYearName()).append("绩效考核，您还有");
                            content.append(stringBuffer.toString()).append("等未完成");
                            content.append("，距离考核截止时间还有");
                            content.append(day).append("天，请尽快处理。");
                            boolean sendResult = SendMsgUtil.sendAssessStartMsg(user.getMobile(), content.toString());
                            if (sendResult) {
                                Msg msg = new Msg();
                                msg.setSendTime(new Date());
                                msg.setMobile(user.getMobile());
                                msg.setMsgType(ProjectConstants.MSG_TYPE_END);
                                msg.setYearId(year.getOid());
                                msg.setUserId(user.getOid());
                                msg.setMsgCode(content.toString());
                                msgMapper.insertSelective(msg);
                            }
                        }
                    }
                }
            }
        }
    }
}