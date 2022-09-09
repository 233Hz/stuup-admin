package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.TeachGroupMapper;
import com.poho.stuup.model.TeachGroup;
import com.poho.stuup.service.ITeachGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Create by 韩超军
 * 2018/4/2 下午4:31
 */
@Service
public class TeachGroupServiceImpl implements ITeachGroupService {

    @Autowired
    private TeachGroupMapper teachGroupMapper;

    @Override
    public int deleteByPrimaryKey(Integer oid) {
        return teachGroupMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(TeachGroup record) {
        return teachGroupMapper.insert(record);
    }

    @Override
    public int insertSelective(TeachGroup record) {
        return teachGroupMapper.insertSelective(record);
    }

    @Override
    public TeachGroup selectByPrimaryKey(Integer oid) {
        return teachGroupMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(TeachGroup record) {
        return teachGroupMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(TeachGroup record) {
        return teachGroupMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findDataPageResult(Integer facultyId, String key, Integer page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("facultyId",facultyId);
        int count = teachGroupMapper.findTotalTeachGroupByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<TeachGroup> list = teachGroupMapper.findTeachGroupPageResultByCond(map);
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
    public ResponseModel saveOrUpdateTeachGroup(TeachGroup teachGroup) {
        ResponseModel model = new ResponseModel();
        TeachGroup check = null;//teachGroupMapper.checkTeachGroup(teachGroup);
        if (check != null) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("教研组名称已存在");
        } else {
            int line = 0;
            if (teachGroup.getOid() != null) {
                line = teachGroupMapper.updateByPrimaryKeySelective(teachGroup);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("更新成功");
            } else {
                line = teachGroupMapper.insertSelective(teachGroup);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("添加成功");
            }
        }
        return model;
    }

    @Override
    public List<TeachGroup> findAllTeachGroup() {
        return teachGroupMapper.findAllTeachGroup();
    }

    @Override
    public TeachGroup findTeachGroupByName(String groupName) {
        return teachGroupMapper.findTeachGroupByName(groupName);
    }

    @Override
    public List<TeachGroup> findTeachGroupByFacultyId(String facultyId) {
        return teachGroupMapper.findTeachGroupByFacultyId(facultyId);
    }
}
