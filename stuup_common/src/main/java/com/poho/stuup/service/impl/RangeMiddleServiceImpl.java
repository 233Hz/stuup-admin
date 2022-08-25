package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.custom.CusMiddleRangeSubmit;
import com.poho.stuup.dao.RangeMiddleMapper;
import com.poho.stuup.model.RangeMiddle;
import com.poho.stuup.service.IRangeMiddleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 21:49 2020/12/27
 * @Modified By:
 */
@Service
public class RangeMiddleServiceImpl implements IRangeMiddleService {
    @Resource
    private RangeMiddleMapper rangeMiddleMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return rangeMiddleMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(RangeMiddle record) {
        return rangeMiddleMapper.insert(record);
    }

    @Override
    public int insertSelective(RangeMiddle record) {
        return rangeMiddleMapper.insertSelective(record);
    }

    @Override
    public RangeMiddle selectByPrimaryKey(Long oid) {
        return rangeMiddleMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(RangeMiddle record) {
        return rangeMiddleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RangeMiddle record) {
        return rangeMiddleMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel saveMultiRange(CusMiddleRangeSubmit rangeSubmit) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("保存成功");
        Map<String, Object> param = new HashMap<>();
        param.put("leaderRangeId", rangeSubmit.getLeaderRangeId());
        rangeMiddleMapper.clearLeaderRangeMiddle(param);
        if (MicrovanUtil.isNotEmpty(rangeSubmit.getRangeIds())) {
            for (Long rangeId : rangeSubmit.getRangeIds()) {
                RangeMiddle rangeMiddle = new RangeMiddle();
                rangeMiddle.setLeaderRangeId(rangeSubmit.getLeaderRangeId());
                rangeMiddle.setAssessRangeId(rangeId);
                rangeMiddle.setCreateUser(rangeSubmit.getCreateUser());
                rangeMiddle.setCreateTime(new Date());
                rangeMiddleMapper.insertSelective(rangeMiddle);
            }
        }
        return model;
    }
}
