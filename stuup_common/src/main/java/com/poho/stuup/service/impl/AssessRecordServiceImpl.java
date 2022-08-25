package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.*;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.AssessRecord;
import com.poho.stuup.model.AssessState;
import com.poho.stuup.model.Dept;
import com.poho.stuup.service.IAssessRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:18 2020/9/2
 * @Modified By:
 */
@Service
public class AssessRecordServiceImpl implements IAssessRecordService {
    @Resource
    private AssessRecordMapper assessRecordMapper;
    @Resource
    private AssessStateMapper assessStateMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private RegisterMapper registerMapper;
    @Resource
    private AssessRangeMapper assessRangeMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return assessRecordMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(AssessRecord record) {
        return assessRecordMapper.insert(record);
    }

    @Override
    public int insertSelective(AssessRecord record) {
        return assessRecordMapper.insertSelective(record);
    }

    @Override
    public AssessRecord selectByPrimaryKey(Long oid) {
        return assessRecordMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(AssessRecord record) {
        return assessRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AssessRecord record) {
        return assessRecordMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel saveOrUpdate(CusAssessSubmit assessSubmit) {
        ResponseModel model = new ResponseModel();
        int line = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", assessSubmit.getYearId());
        param.put("assessType", assessSubmit.getAssessType());
        param.put("assessUser", assessSubmit.getAssessUser());
        if (MicrovanUtil.isNotEmpty(assessSubmit.getAssessUsers())) {
            for (CusAssessUser user : assessSubmit.getAssessUsers()) {
                param.put("userId", user.getUserId());
                List<CusNormScore> normScores = user.getScores();
                if (MicrovanUtil.isNotEmpty(normScores)) {
                    for (CusNormScore normScore : normScores) {
                        if (normScore.getNormId().longValue() != ProjectConstants.TEMP_COUNT_ID) {
                            param.put("normId", normScore.getNormId());
                            AssessRecord assessRecord = assessRecordMapper.checkAssessRecord(param);
                            if (MicrovanUtil.isNotEmpty(assessRecord)) {
                                assessRecord.setScore(normScore.getScore());
                                assessRecord.setState(assessSubmit.getState());
                                line = assessRecordMapper.updateByPrimaryKeySelective(assessRecord);
                            } else {
                                assessRecord = new AssessRecord();
                                assessRecord.setYearId(assessSubmit.getYearId());
                                assessRecord.setUserId(user.getUserId());
                                assessRecord.setAssessType(assessSubmit.getAssessType());
                                assessRecord.setNormId(normScore.getNormId());
                                assessRecord.setScore(normScore.getScore());
                                assessRecord.setState(assessSubmit.getState());
                                assessRecord.setAssessUser(assessSubmit.getAssessUser());
                                assessRecord.setCreateTime(new Date());
                                line = assessRecordMapper.insertSelective(assessRecord);
                            }
                        }
                    }
                }
            }
        }
        String method = assessSubmit.getState().intValue() == 1 ? "暂存" : "提交";
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage(method + "成功");
            param.clear();
            param.put("yearId", assessSubmit.getYearId());
            param.put("assessUser", assessSubmit.getAssessUser());
            param.put("assessType", assessSubmit.getAssessType());
            AssessState assessState = assessStateMapper.checkAssessState(param);
            if (MicrovanUtil.isNotEmpty(assessState)) {
                assessState.setState(assessSubmit.getState());
                assessStateMapper.updateByPrimaryKeySelective(assessState);
            } else {
                assessState = new AssessState();
                assessState.setYearId(assessSubmit.getYearId());
                assessState.setAssessUser(assessSubmit.getAssessUser());
                assessState.setAssessType(assessSubmit.getAssessType());
                assessState.setState(assessSubmit.getState());
                assessState.setCreateTime(new Date());
                assessStateMapper.insertSelective(assessState);
            }
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage(method + "失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel saveOrUpdateTeacher(CusTeacherSubmit teacherSubmit) {
        ResponseModel model = new ResponseModel();
        int line = 0;
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", teacherSubmit.getYearId());
        param.put("assessType", teacherSubmit.getAssessType());
        param.put("assessUser", teacherSubmit.getAssessUser());
        List<CusAssessTeacher> cusAssessTeachers = teacherSubmit.getAssessTeachers();
        if (teacherSubmit.getState().intValue() == ProjectConstants.ASSESS_STATE_SAVE) {
            if (MicrovanUtil.isNotEmpty(cusAssessTeachers)) {
                for (CusAssessTeacher assessTeacher : teacherSubmit.getAssessTeachers()) {
                    param.put("userId", assessTeacher.getUserId());
                    AssessRecord assessRecord = assessRecordMapper.checkAssessRecord(param);
                    if (MicrovanUtil.isNotEmpty(assessRecord)) {
                        assessRecord.setScore(assessTeacher.getScore());
                        assessRecord.setState(teacherSubmit.getState());
                        line = assessRecordMapper.updateByPrimaryKeySelective(assessRecord);
                    } else {
                        assessRecord = new AssessRecord();
                        assessRecord.setYearId(teacherSubmit.getYearId());
                        assessRecord.setUserId(assessTeacher.getUserId());
                        assessRecord.setAssessType(teacherSubmit.getAssessType());
                        assessRecord.setScore(assessTeacher.getScore());
                        assessRecord.setState(teacherSubmit.getState());
                        assessRecord.setAssessUser(teacherSubmit.getAssessUser());
                        assessRecord.setCreateTime(new Date());
                        line = assessRecordMapper.insertSelective(assessRecord);
                    }
                }
            }
        } else {
            line = assessRecordMapper.updateAssessTeacherState(param);
        }
        String method = teacherSubmit.getState() == 1 ? "暂存" : "提交";
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage(method + "成功");
            param.clear();
            param.put("yearId", teacherSubmit.getYearId());
            param.put("assessUser", teacherSubmit.getAssessUser());
            param.put("assessType", teacherSubmit.getAssessType());
            AssessState assessState = assessStateMapper.checkAssessState(param);
            if (MicrovanUtil.isNotEmpty(assessState)) {
                assessState.setState(teacherSubmit.getState());
                assessStateMapper.updateByPrimaryKeySelective(assessState);
            } else {
                assessState = new AssessState();
                assessState.setYearId(teacherSubmit.getYearId());
                assessState.setAssessUser(teacherSubmit.getAssessUser());
                assessState.setAssessType(teacherSubmit.getAssessType());
                assessState.setState(teacherSubmit.getState());
                assessState.setCreateTime(new Date());
                assessStateMapper.insertSelective(assessState);
            }
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage(method + "失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel findStaffData(Long yearId, Long deptId, String key) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> param = new HashMap<>();
        param.put("key", key);
        if (MicrovanUtil.isNotEmpty(yearId)) {
            param.put("yearId", yearId);
        }
        if (MicrovanUtil.isNotEmpty(deptId)) {
            param.put("deptId", deptId);
        }
        param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCPYG);
        List<AssessRecord> assessRecords = assessRecordMapper.queryList(param);
        if (MicrovanUtil.isNotEmpty(assessRecords)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            model.setData(assessRecords);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("暂无数据");
        }
        return model;
    }

    @Override
    public ResponseModel adjustScore(AssessRecord record) {
        ResponseModel model = new ResponseModel();
        if (MicrovanUtil.isEmpty(record.getAdjustScore())) {
            record.setAdjustScore(null);
        }
        int line = assessRecordMapper.updateAdjustScore(record);
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("调整成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("调整失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel findStaffCountData(Long yearId) {
        ResponseModel model = new ResponseModel();
        List<Dept> deptList = deptMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(deptList)) {
            List<CusStaffCount> staffCounts = new ArrayList<>();
            int countA = 0;
            int countB = 0;
            int countTotal = 0;
            for (Dept dept : deptList) {
                CusStaffCount staffCount = new CusStaffCount();
                staffCount.setDeptName(dept.getDeptName());
                Map<String, Object> param = new HashMap<>();
                param.put("yearId", yearId);
                param.put("deptId", dept.getOid());
                param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCPYG);
                param.put("score", 1);
                int totalA = assessRecordMapper.queryTotal(param);
                countA = countA + totalA;
                staffCount.setTotalA(totalA);
                param.put("score", 2);
                int totalB = assessRecordMapper.queryTotal(param);
                countB = countB + totalB;
                staffCount.setTotalB(totalB);
                staffCount.setCount(totalA + totalB);
                countTotal = countTotal + staffCount.getCount();
                staffCounts.add(staffCount);
            }

            CusStaffCount staffCount = new CusStaffCount();
            staffCount.setDeptName("总计");
            staffCount.setTotalA(countA);
            staffCount.setTotalB(countB);
            staffCount.setCount(countTotal);
            staffCounts.add(staffCount);

            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            model.setData(staffCounts);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("无数据");
        }
        return model;
    }

    @Override
    public ResponseModel findScheduleData(Long yearId, Long deptId) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        CusScheduleData scheduleData = new CusScheduleData();

        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        if (MicrovanUtil.isNotEmpty(deptId)) {
            param.put("deptId", deptId);
            scheduleData.setDeptId(deptId);
        }

        param.put("userType", ProjectConstants.RANGE_TYPE_DZLD);
        int total = userMapper.findAssessUserTotal(param);
        param.put("assessType", ProjectConstants.ASSESS_TYPE_DZLDPZC);
        int assessTotal = assessRecordMapper.findAssessTotal(param);
        String per = MicrovanUtil.convertDivideTwoPoint(assessTotal, total);
        scheduleData.setDzldPer(Double.parseDouble(per));

        param.put("userType", ProjectConstants.RANGE_TYPE_FGLD);
        total = userMapper.findAssessUserTotal(param);
        param.put("assessType", ProjectConstants.ASSESS_TYPE_FGLDPZC);
        assessTotal = assessRecordMapper.findAssessTotal(param);
        per = MicrovanUtil.convertDivideTwoPoint(assessTotal, total);
        scheduleData.setFgldPer(Double.parseDouble(per));

        param.put("userType", ProjectConstants.RANGE_TYPE_BMFZR);
        total = userMapper.findAssessUserTotal(param);
        param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCPYG);
        assessTotal = assessRecordMapper.findAssessTotal(param);
        per = MicrovanUtil.convertDivideTwoPoint(assessTotal, total);
        scheduleData.setYgPer(Double.parseDouble(per));

        param.put("userType", ProjectConstants.RANGE_TYPE_SYZC);
        total = userMapper.findAssessUserTotal(param);
        param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCZPHP);
        assessTotal = assessRecordMapper.findAssessTotal(param);
        per = MicrovanUtil.convertDivideTwoPoint(assessTotal, total);
        scheduleData.setZphpPer(Double.parseDouble(per));

        param.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
        total = userMapper.findAssessUserTotal(param);
        param.put("assessType", ProjectConstants.ASSESS_TYPE_QZPZC);
        assessTotal = assessRecordMapper.findAssessTotal(param);
        per = MicrovanUtil.convertDivideTwoPoint(assessTotal, total);
        scheduleData.setQzcpPer(Double.parseDouble(per));

        total = userMapper.findAssessZCYGTotal(param);
        assessTotal = registerMapper.findRegTotal(param);
        per = MicrovanUtil.convertDivideTwoPoint(assessTotal, total);
        scheduleData.setKhdjPer(Double.parseDouble(per));

        model.setData(scheduleData);
        return model;
    }

    @Override
    public ResponseModel findScheduleComData(Long yearId, Integer assessType) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<Dept> deptList = deptMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(deptList)) {
            List<CusMap> data = new ArrayList<>();
            for (Dept dept : deptList) {
                Map<String, Object> param = new HashMap<>();
                param.put("yearId", yearId);
                param.put("deptId", dept.getOid());
                int userType = 0;
                if (assessType.intValue() == ProjectConstants.ASSESS_TYPE_DZLDPZC) {
                    userType = ProjectConstants.RANGE_TYPE_DZLD;
                }
                if (assessType.intValue() == ProjectConstants.ASSESS_TYPE_FGLDPZC) {
                    userType = ProjectConstants.RANGE_TYPE_FGLD;
                }
                if (assessType.intValue() == ProjectConstants.ASSESS_TYPE_ZCPYG) {
                    userType = ProjectConstants.RANGE_TYPE_BMFZR;
                }
                if (assessType.intValue() == ProjectConstants.ASSESS_TYPE_ZCZPHP) {
                    userType = ProjectConstants.RANGE_TYPE_SYZC;
                }
                if (assessType.intValue() == ProjectConstants.ASSESS_TYPE_QZPZC) {
                    userType = ProjectConstants.RANGE_TYPE_PTYG;
                }
                param.put("userType", userType);
                int total = userMapper.findAssessUserTotal(param);
                param.put("assessType", assessType);
                int assessTotal = assessRecordMapper.findAssessTotal(param);
                String per = MicrovanUtil.convertDivideTwoPoint(assessTotal, total);
                data.add(new CusMap(dept.getDeptName(), Double.parseDouble(per)));
            }
            model.setData(data);
        }
        return model;
    }

    @Override
    public ResponseModel findAssessItems(Long yearId, Long userId, Integer assessType) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        param.put("userId", userId);
        param.put("assessType", assessType);
        List<AssessRecord> assessRecords = assessRecordMapper.findAssessResult(param);
        model.setData(assessRecords);
        return model;
    }

    @Override
    public ResponseModel queryCanSubmit(Long yearId, Long deptId, Long assessUser) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        param.put("deptId", deptId);
        param.put("assessUser", assessUser);
        param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCPYG);
        int total = assessRecordMapper.findAssessUserTotal(param);
        //TODO 上面的语名有bug 需要认真看，现在前段页面提交按钮是否出现检查有问题
        param.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
        int userTotal = assessRangeMapper.queryTotal(param);
        if (total >= userTotal) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("可提交");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("不可提交");
        }
        return model;
    }
}
