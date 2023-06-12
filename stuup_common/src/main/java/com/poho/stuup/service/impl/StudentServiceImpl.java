package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.Student;
import com.poho.stuup.service.IStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentServiceImpl implements IStudentService {
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public ResponseModel findDataPageResult(String gradeId, String majorId, String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("gradeId", gradeId);
        map.put("majorId", majorId);
        int count = studentMapper.findTotalStudentByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Student> list = studentMapper.findStudentPageResultByCond(map);
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
    public Student getStudentForStudentNO(String studentNo) {
        return studentMapper.getStudentForStudentNO(studentNo);
    }

    @Override
    public List<Integer> getAllStudentId() {
        return studentMapper.getAllStudentId();
    }
}
