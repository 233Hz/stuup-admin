package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.AssessStateMapper;
import com.poho.stuup.dao.OperRecordMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.AssessState;
import com.poho.stuup.model.OperRecord;
import com.poho.stuup.model.Year;
import com.poho.stuup.service.IAssessStateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 20:41 2020/12/13
 * @Modified By:
 */
@Service
public class AssessStateServiceImpl implements IAssessStateService {
    @Resource
    private AssessStateMapper assessStateMapper;
    @Resource
    private OperRecordMapper operRecordMapper;
    @Resource
    private YearMapper yearMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return assessStateMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(AssessState record) {
        return assessStateMapper.insert(record);
    }

    @Override
    public int insertSelective(AssessState record) {
        return assessStateMapper.insertSelective(record);
    }

    @Override
    public AssessState selectByPrimaryKey(Long oid) {
        return assessStateMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(AssessState record) {
        return assessStateMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AssessState record) {
        return assessStateMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findAssessState(Long yearId, Long userId, String assessType) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("开始时间未到");
        Year year = yearMapper.selectByPrimaryKey(yearId);
        if (MicrovanUtil.isNotEmpty(year)) {
            long now = System.currentTimeMillis();
            long start = year.getYearStart().getTime();
            long end = year.getYearEnd().getTime();
            if (start > now) {
                model.setMessage("开始时间未到");
            } else if (now >= start && now <= end) {
                Map<String, Object> param = new HashMap<>();
                param.put("yearId", yearId);
                param.put("operType", ProjectConstants.OPER_TYPE_SEND_NORM);
                OperRecord operRecord = operRecordMapper.checkOperRecord(param);
                if (MicrovanUtil.isNotEmpty(operRecord)) {
                    param.put("operType", ProjectConstants.OPER_TYPE_RESULT);
                    operRecord = operRecordMapper.checkOperRecord(param);
                    if (MicrovanUtil.isNotEmpty(operRecord)) {
                        model.setMessage("考核已结束");
                    } else if (MicrovanUtil.isNotEmpty(assessType)){
                        param.clear();
                        param.put("yearId", yearId);
                        param.put("assessUser", userId);
                        param.put("assessType", assessType);
                        AssessState assessState = assessStateMapper.checkAssessState(param);
                        if (MicrovanUtil.isNotEmpty(assessState) && assessState.getState().intValue() == ProjectConstants.ASSESS_STATE_SUBMIT) {
                            model.setMessage("您的评分已提交");
                        } else {
                            model.setCode(CommonConstants.CODE_SUCCESS);
                            model.setMessage("考核开始");
                        }
                    } else {
                        model.setCode(CommonConstants.CODE_SUCCESS);
                        model.setMessage("考核开始");
                    }
                } else {
                    model.setMessage("测评表未下发");
                }
            } else if (now > end){
                model.setMessage("考核已结束");
            }
        }
        return model;

    }
}
