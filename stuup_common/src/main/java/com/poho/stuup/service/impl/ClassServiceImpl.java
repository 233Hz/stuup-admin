package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.service.IClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassServiceImpl implements IClassService {
    @Autowired
    private ClassMapper classMapper;
    @Autowired
    private TeacherMapper teacherMapper;
    @Autowired
    private FacultyMapper facultyMapper;
    @Autowired
    private MajorMapper majorMapper;
    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public int updateByPrimaryKeySelective(Class clazz) {
        return classMapper.updateByPrimaryKeySelective(clazz);
    }

    @Override
    public List<Class> findAllClass(Map<String, Object> map) {
        return classMapper.findAllClass(map);
    }

    @Override
    public ResponseModel findDataPageResult(String key, Integer facultyId, Integer gradeId, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("facultyId", facultyId);
        map.put("gradeId", gradeId);
        int count = classMapper.findTotalClassByCond(map);
        PageData pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<Class> list = classMapper.findClassPageResultByCond(map);
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
    public ResponseModel saveOrUpdate(Class clazz) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> params = new HashMap();
        params.put("id", clazz.getId());
        params.put("num", clazz.getNum());
        params.put("grade", clazz.getGradeId());
        int count = classMapper.findClass(params);
        if (count > 0) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("班级信息已存在");
            return model;
        }
        if (clazz.getId() != null) {
            int line = classMapper.updateByPrimaryKeySelective(clazz);
            if (line <= 0) {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMessage("修改失败");
            } else {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("修改成功");
            }
        } else {
            int line = classMapper.insertSelective(clazz);
            if (line <= 0) {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMessage("添加失败");
            } else {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("添加成功");
            }
        }
        return model;
    }

    @Override
    public List<Class> findClassByGrade(String grade) {
        return classMapper.findClassByGrade(grade);
    }

    @Override
    public boolean deleteClass(String ids) {
        List<String> idList = new ArrayList<>();
        if (!MicrovanUtil.isEmpty(ids)) {
            String[] idsArr = ids.split(",");
            for (String id: idsArr) {
                idList.add(id);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ids", idList);
        return classMapper.deleteClass(map) > 0 ? true : false;
    }

    @Override
    public Map<String, Object> addImportClasses(List<Class> classes) {
        Map<String, Object> map = new HashMap<>();
        int j = 0;
        int k = 0;
        StringBuffer msg = new StringBuffer();
        for (int i = 0; i < classes.size(); i++) {
            Class clazz = classes.get(i);
            clazz.setIsValid(1);
            boolean isVia = true;
            StringBuffer itemMsg = new StringBuffer();
            itemMsg.append("第").append(clazz.getRowNum()).append("行：");
            if (MicrovanUtil.isEmpty(clazz.getName())) {
                itemMsg.append("班级名称为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(clazz.getCode())) {
                itemMsg.append("班号为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(clazz.getTeacherNo())) {
                itemMsg.append("班主任工号为空；");
                isVia = false;
            }
            else {
                Teacher teacher = teacherMapper.findTeacherByJobNo(clazz.getTeacherNo());
                if (teacher != null) {
                    clazz.setTeacherId(teacher.getId());
                }
            }
            if (MicrovanUtil.isEmpty(clazz.getGradeName())) {
                itemMsg.append("年级为空；");
                isVia = false;
            }
            else {
                Grade grade = gradeMapper.findGradeByYear(clazz.getGradeName());
                if (grade != null) {
                    clazz.setGradeId(grade.getOid());
                }else {
                    itemMsg.append("没有该年级信息；");
                    isVia = false;
                }
            }
            if (MicrovanUtil.isEmpty(clazz.getFacultyName())) {
                itemMsg.append("系部名称为空；");
                isVia = false;
            }
            else {
                Faculty faculty = facultyMapper.findFacultyByName(clazz.getFacultyName());
                if (faculty != null) {
                    clazz.setFacultyId(faculty.getOid());
                }
            }
            if (MicrovanUtil.isEmpty(clazz.getMajorName())) {
                itemMsg.append("专业名称为空；");
                isVia = false;
            }
            else {
                Major major = majorMapper.findMajorByName(clazz.getMajorName());
                if (major != null) {
                    clazz.setMajorId(major.getOid());
                }
            }
            if (MicrovanUtil.isEmpty(clazz.getJoinExam())) {
                itemMsg.append("参与排考为空；");
                isVia = false;
            }
            if (isVia) {
                int line = 0;
                Map<String, Object> paramMap = new HashMap();
                paramMap.put("name", clazz.getName());
                paramMap.put("grade", clazz.getGradeId());
                Class checkClazz = classMapper.checkClass(paramMap);
                if (checkClazz != null) {
                    clazz.setId(checkClazz.getId());
                    clazz.setIsValid(1);
                    line = classMapper.updateByPrimaryKeySelective(clazz);
                } else {
                    line = classMapper.insertSelective(clazz);
                }
                if (line > 0) {
                    j++;
                } else {
                    msg.append("第").append(clazz.getRowNum()).append("行，插入失败；");
                    k++;
                }
            } else {
                msg.append(itemMsg.toString()).append("。");
                k++;
            }
        }
        map.put("okNum", j);
        map.put("failNum", k);
        map.put("msg", msg.toString());
        return map;
    }

    @Override
    public Class findClassById(Integer classId) {
        return classMapper.selectByPrimaryKey(classId);
    }

    @Override
    public String findClassNamesByIds(String ids) {
        StringBuffer classNames = new StringBuffer();
        if (!MicrovanUtil.isEmpty(ids)) {
            String[] idArr = ids.split(",");
            for (String id : idArr) {
                Class clazz = classMapper.selectByPrimaryKey(Integer.valueOf(id));
                if (clazz != null) {
                    classNames.append(clazz.getName()).append(",");
                }
            }
            return classNames.toString();
        }
        return "";
    }

    @Override
    public List<Class> findClassByAdminTeacher(Integer id) {
        return classMapper.findClassByAdmin(id);
    }
}
