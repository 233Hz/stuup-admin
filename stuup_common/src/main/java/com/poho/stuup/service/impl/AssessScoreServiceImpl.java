package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.AssessRecordMapper;
import com.poho.stuup.dao.AssessScoreMapper;
import com.poho.stuup.dao.OperRecordMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.AssessRecord;
import com.poho.stuup.model.AssessScore;
import com.poho.stuup.model.OperRecord;
import com.poho.stuup.model.User;
import com.poho.stuup.service.IAssessScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 15:10 2020/10/15
 * @Modified By:
 */
@Service
public class AssessScoreServiceImpl implements IAssessScoreService {
    @Resource
    private AssessScoreMapper assessScoreMapper;
    @Resource
    private AssessRecordMapper assessRecordMapper;
    @Resource
    private OperRecordMapper operRecordMapper;
    @Resource
    private UserMapper userMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return assessScoreMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(AssessScore record) {
        return assessScoreMapper.insert(record);
    }

    @Override
    public int insertSelective(AssessScore record) {
        return assessScoreMapper.insertSelective(record);
    }

    @Override
    public AssessScore selectByPrimaryKey(Long oid) {
        return assessScoreMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(AssessScore record) {
        return assessScoreMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AssessScore record) {
        return assessScoreMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findData(Long yearId, Long deptId, String key) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> param = new HashMap<>();
        param.put("key", key);
        if (MicrovanUtil.isNotEmpty(yearId)) {
            param.put("yearId", yearId);
        }
        if (MicrovanUtil.isNotEmpty(deptId)) {
            param.put("deptId", deptId);
        }
        List<AssessScore> assessScores = assessScoreMapper.queryList(param);
        if (MicrovanUtil.isNotEmpty(assessScores)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            //计算平均分和总分
            assessScores = calculateAssessScores(assessScores);
            model.setData(assessScores);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("无数据");
        }
        return model;
    }

    /**
     * 计算平均分和总分
     * @param assessScores
     * @return
     */
    private List<AssessScore> calculateAssessScores(List<AssessScore> assessScores) {
        if (MicrovanUtil.isNotEmpty(assessScores)) {
            double totalQzcp = 0.0;
            double totalZphp = 0.0;
            double totalFgld = 0.0;
            double totalDzld = 0.0;
            double totalScore = 0.0;

            for (int i = 0; i < assessScores.size(); i++) {
                AssessScore assessScore = assessScores.get(i);
                assessScore.setRank((i + 1) + "");
                totalQzcp = totalQzcp + assessScore.getQzcp();
                totalZphp = totalZphp + assessScore.getZphp();
                totalFgld = totalFgld + assessScore.getFgld();
                totalDzld = totalDzld + assessScore.getDzld();
                totalScore = totalScore + assessScore.getScore();
            }

            AssessScore assessScore = new AssessScore();
            int total = assessScores.size();
            assessScore.setOid(0L);
            assessScore.setYearName("平均");
            assessScore.setQzcp(MicrovanUtil.convertDivideTwoPoint(totalQzcp, total));
            assessScore.setZphp(MicrovanUtil.convertDivideTwoPoint(totalZphp, total));
            assessScore.setFgld(MicrovanUtil.convertDivideTwoPoint(totalFgld, total));
            assessScore.setDzld(MicrovanUtil.convertDivideTwoPoint(totalDzld, total));
            assessScore.setScore(MicrovanUtil.convertDivideTwoPoint(totalScore, total));
            assessScores.add(assessScore);

            assessScore = new AssessScore();
            assessScore.setOid(0L);
            assessScore.setYearName("合计");
            assessScore.setQzcp(MicrovanUtil.convertDouble(totalQzcp));
            assessScore.setZphp(MicrovanUtil.convertDouble(totalZphp));
            assessScore.setFgld(MicrovanUtil.convertDouble(totalFgld));
            assessScore.setDzld(MicrovanUtil.convertDouble(totalDzld));
            assessScore.setScore(MicrovanUtil.convertDouble(totalScore));
            assessScores.add(assessScore);
        }
        return assessScores;
    }

    @Override
    public ResponseModel generateData(String yearId, Long operUser) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("生成成功");
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", Long.valueOf(yearId));
        assessScoreMapper.delYearScore(Long.valueOf(yearId));
        param.put("operType", ProjectConstants.OPER_TYPE_RESULT);
        operRecordMapper.clearOperRecord(param);
        List<User> users = userMapper.findAssessMiddleUsers(param);
        if (MicrovanUtil.isNotEmpty(users)) {
            for (User user : users) {
                AssessScore assessScore = new AssessScore();
                assessScore.setYearId(Long.valueOf(yearId));
                assessScore.setUserId(user.getOid());
                assessScore.setCreateTime(new Date());
                param.put("userId", user.getOid());
                //计算群众测评的分数
                param.put("assessType", ProjectConstants.ASSESS_TYPE_QZPZC);
                List<AssessRecord> assessRecords = assessRecordMapper.findAssessResult(param);
                if (MicrovanUtil.isNotEmpty(assessRecords)) {
                    assessScore.setQzcprs(assessRecords.size());
                    assessScore.setQzcp(ProjectUtil.calculateAssessScore(assessRecords, ProjectConstants.SCALE_QZCP));
                } else {
                    assessScore.setQzcprs(0);
                    assessScore.setQzcp(0.0);
                }
                //计算自评互评的分数
                param.put("assessType", ProjectConstants.ASSESS_TYPE_ZCZPHP);
                assessRecords = assessRecordMapper.findAssessResult(param);
                if (MicrovanUtil.isNotEmpty(assessRecords)) {
                    assessScore.setZphprs(assessRecords.size());
                    assessScore.setZphp(ProjectUtil.calculateAssessScore(assessRecords, ProjectConstants.SCALE_ZPHP));
                } else {
                    assessScore.setZphprs(0);
                    assessScore.setZphp(0.0);
                }
                //计算分管领导评价的分数
                param.put("assessType", ProjectConstants.ASSESS_TYPE_FGLDPZC);
                assessRecords = assessRecordMapper.findAssessResult(param);
                if (MicrovanUtil.isNotEmpty(assessRecords)) {
                    assessScore.setFgldrs(assessRecords.size());
                    assessScore.setFgld(ProjectUtil.calculateAssessScore(assessRecords, ProjectConstants.SCALE_FGLD));
                } else {
                    assessScore.setFgldrs(0);
                    assessScore.setFgld(0.0);
                }
                //计算党政领导评价的分数
                param.put("assessType", ProjectConstants.ASSESS_TYPE_DZLDPZC);
                assessRecords = assessRecordMapper.findAssessResult(param);
                if (MicrovanUtil.isNotEmpty(assessRecords)) {
                    assessScore.setDzldrs(assessRecords.size());
                    assessScore.setDzld(ProjectUtil.calculateAssessScore(assessRecords, ProjectConstants.SCALE_DZLD));
                } else {
                    assessScore.setDzldrs(0);
                    assessScore.setDzld(0.0);
                }
                double score = assessScore.getQzcp().doubleValue() + assessScore.getZphp().doubleValue();
                score = score + assessScore.getFgld().doubleValue() + assessScore.getDzld().doubleValue();
                assessScore.setScore(score);
                assessScoreMapper.insertSelective(assessScore);
            }
            OperRecord operRecord = new OperRecord();
            operRecord.setOperUser(operUser);
            operRecord.setOperTime(new Date());
            operRecord.setYearId(Long.valueOf(yearId));
            operRecord.setOperType(ProjectConstants.OPER_TYPE_RESULT);
            operRecordMapper.insertSelective(operRecord);
        }
        return model;
    }

    @Override
    public ResponseModel adjustAssessScore(AssessScore score) {
        ResponseModel model = new ResponseModel();
        if (MicrovanUtil.isEmpty(score.getAdjustScore())) {
            score.setAdjustScore(null);
        }
        int line = assessScoreMapper.updateAdjustScore(score);
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("保存失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel adjustAssessQzcp(AssessScore score) {
        ResponseModel model = new ResponseModel();
        if (MicrovanUtil.isEmpty(score.getAdjustQzcp())) {
            score.setAdjustQzcp(null);
        }
        int line = assessScoreMapper.updateAdjustQzcp(score);
        if (line > 0) {
            assessScoreMapper.reCalculateScore(score.getOid());
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("保存失败，请稍后重试");
        }
        return model;
    }
}
