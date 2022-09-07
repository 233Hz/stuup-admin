package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.MajorMapper;
import com.poho.stuup.model.Major;
import com.poho.stuup.service.IMajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MajorServiceImpl implements IMajorService {
    @Autowired
    private MajorMapper majorMapper;

    @Override
    public int deleteByPrimaryKey(Integer oid) {
        return majorMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Major record) {
        return majorMapper.insert(record);
    }

    @Override
    public int insertSelective(Major record) {
        return majorMapper.insertSelective(record);
    }

    @Override
    public Major selectByPrimaryKey(Integer oid) {
        return majorMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Major record) {
        return majorMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Major record) {
        return majorMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findPageResult(String key, Integer page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        int count = majorMapper.findTotalMajorByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Major> list = majorMapper.findMajorPageResultByCond(map);
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
    public ResponseModel saveOrUpdate(Major major) {
        ResponseModel model = new ResponseModel();
        Major checkMajor = majorMapper.checkMajor(major);
        if (checkMajor != null) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("专业名称已存在");
        } else {
            int line = 0;
            if (major.getOid() != null) {
                line = majorMapper.updateByPrimaryKeySelective(major);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("更新成功");
            } else {
                line = majorMapper.insertSelective(major);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("添加成功");
            }
        }
        return model;
    }

    @Override
    public List<Major> findMajors() {
        return majorMapper.findMajors();
    }
}
