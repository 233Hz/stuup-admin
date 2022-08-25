package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.OperRecordMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.OperRecord;
import com.poho.stuup.model.Year;
import com.poho.stuup.service.IOperRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 15:01 2020/9/18
 * @Modified By:
 */
@Service
public class OperRecordServiceImpl implements IOperRecordService {
    @Resource
    private OperRecordMapper operRecordMapper;
    @Resource
    private YearMapper yearMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return operRecordMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(OperRecord record) {
        return operRecordMapper.insert(record);
    }

    @Override
    public int insertSelective(OperRecord record) {
        return operRecordMapper.insertSelective(record);
    }

    @Override
    public OperRecord selectByPrimaryKey(Long oid) {
        return operRecordMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(OperRecord record) {
        return operRecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OperRecord record) {
        return operRecordMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findOperData(Long yearId, Integer operType) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        param.put("operType", operType);
        OperRecord checkOperRecord = operRecordMapper.checkOperRecord(param);
        if (MicrovanUtil.isNotEmpty(checkOperRecord)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            model.setData(checkOperRecord);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("暂未下发");
        }
        return model;
    }

    @Override
    public ResponseModel sendNorm(OperRecord record) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("下发失败，请稍后重试");
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", record.getYearId());
        param.put("operType", record.getOperType());
        OperRecord checkOperRecord = operRecordMapper.checkOperRecord(param);
        if (MicrovanUtil.isEmpty(checkOperRecord)) {
            record.setOperTime(new Date());
            int line = operRecordMapper.insertSelective(record);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("下发成功");
            }
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("已下发");
        }
        return model;
    }

    @Override
    public ResponseModel findAssessState(Long yearId) {
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
