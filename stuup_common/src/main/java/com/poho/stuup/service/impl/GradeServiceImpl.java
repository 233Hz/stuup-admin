package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.GradeMapper;
import com.poho.stuup.model.Grade;
import com.poho.stuup.service.IGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements IGradeService {
    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public int deleteByPrimaryKey(Integer oid) {
        return gradeMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Grade record) {
        return gradeMapper.insert(record);
    }

    @Override
    public int insertSelective(Grade record) {
        return gradeMapper.insertSelective(record);
    }

    @Override
    public Grade selectByPrimaryKey(Integer oid) {
        return gradeMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Grade record) {
        return gradeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Grade record) {
        return gradeMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Grade> findGrades() {
        return gradeMapper.findGrades();
    }

    @Override
    public ResponseModel findDataPageResult(String key, Integer page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        int count = gradeMapper.findTotalGradeByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Grade> list = gradeMapper.findGradePageResultByCond(map);
        if (MicrovanUtil.isNotEmpty(list)) {
            pageData.setRecords(list);
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("无数据");
        }
        model.setData(pageData);
        return model;
    }

    @Override
    public ResponseModel saveOrUpdateGrade(Grade grade) {
        ResponseModel model = new ResponseModel();
        Grade checkGrade = gradeMapper.checkGrade(grade);
        if (checkGrade != null) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("年级名称已存在");
        } else {
            int line = 0;
            if (grade.getOid() != null) {
                line = gradeMapper.updateByPrimaryKeySelective(grade);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("更新成功");
            } else {
                line = gradeMapper.insertSelective(grade);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("添加成功");
            }
        }
        return model;
    }

    @Override
    public Map<Integer, Grade> gradeMap() {
        List<Grade> gradeList = gradeMapper.selectAll();
        return gradeList.stream().collect(Collectors.toMap(Grade::getOid, Function.identity()));
    }
}
