package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Teacher;
import com.poho.stuup.service.ITeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements ITeacherService {
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private TermMapper termMapper;
    @Autowired
    private FacultyMapper facultyMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private ConfigMapper configMapper;

    @Override
    public int insert(Teacher record) {
        return teacherMapper.insert(record);
    }

    @Override
    public int insertSelective(Teacher record) {
        return teacherMapper.insertSelective(record);
    }

    @Override
    public Teacher selectByPrimaryKey(Integer id) {
        return teacherMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(Teacher record) {
        return teacherMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Teacher record) {
        return teacherMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<Teacher> findTeachers(Integer facultyId) {
        return teacherMapper.findTeachers(facultyId);
    }

    @Override
    public ResponseModel findDataPageResult(Integer facultyId, String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("facultyId", facultyId);
        int count = teacherMapper.findTotalTeacherByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Teacher> list = teacherMapper.findTeacherPageResultByCond(map);
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
    public List<Map<String, Object>> searchTeacher(String key) {
        List<Teacher> teachers = teacherMapper.findTeacherByKey(key);
        if (teachers != null && teachers.size() > 0) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (Teacher teacher : teachers) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", teacher.getId());
                map.put("name", teacher.getName());
                list.add(map);
            }
            return list;
        }
        return null;
    }

    @Override
    public Map<Integer, Teacher> teacherMap() {
        List<Teacher> teacherList = teacherMapper.selectAll();
        return teacherList.stream().collect(Collectors.toMap(Teacher::getId, Function.identity()));
    }


}
