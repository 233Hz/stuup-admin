package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.FacultyMapper;
import com.poho.stuup.model.Faculty;
import com.poho.stuup.service.IFacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacultyServiceImpl implements IFacultyService {
    @Autowired
    private FacultyMapper facultyMapper;

    @Override
    public int deleteByPrimaryKey(Integer oid) {
        return facultyMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Faculty record) {
        return facultyMapper.insert(record);
    }

    @Override
    public int insertSelective(Faculty record) {
        return facultyMapper.insertSelective(record);
    }

    @Override
    public Faculty selectByPrimaryKey(Integer oid) {
        return facultyMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Faculty record) {
        return facultyMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Faculty record) {
        return facultyMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Faculty> findAllFaculty() {
        return facultyMapper.findAllFaculty();
    }

    @Override
    public ResponseModel findDataPageResult(String key, int page, int pageSize)  {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        int count = facultyMapper.findTotalFacultyByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Faculty> list = facultyMapper.findFacultyPageResultByCond(map);
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
    public ResponseModel saveOrUpdate(Faculty faculty) {
        ResponseModel model = new ResponseModel();
        Faculty checkFaculty = facultyMapper.checkFaculty(faculty);
        if (checkFaculty != null) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("系部名称已存在");
        }
        else {
            int line = 0;
            if (faculty.getOid() != null) {
                line = facultyMapper.updateByPrimaryKeySelective(faculty) ;
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("更新成功");
            } else {
                faculty.setCreateTime(new Date());
                line = facultyMapper.insertSelective(faculty);
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("添加成功");
            }
        }
        return model;
    }

    @Override
    public Faculty findFacultyByName(String facultyName) {
        return facultyMapper.findFacultyByName(facultyName);
    }

    @Override
    public List<Faculty> findFacultyByAdmin(Integer id) {
        return facultyMapper.findFacultyByAdmin(id);
    }
}
