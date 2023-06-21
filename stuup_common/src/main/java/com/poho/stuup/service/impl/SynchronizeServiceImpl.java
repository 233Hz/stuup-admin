package com.poho.stuup.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.poho.common.util.*;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.service.ISynchronizeService;
import com.poho.stuup.util.HttpJSONUtil;
import com.poho.stuup.util.ProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.soap.SAAJResult;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author wupeng
 * @Description 同步数据处理类
 * @Date 2020-07-22 21:33
 * @return
 */
@Service
public class SynchronizeServiceImpl implements ISynchronizeService {

    private static final Logger logger = LoggerFactory.getLogger(SynchronizeServiceImpl.class);
    @Resource
    private StudentMapper studentMapper;
    @Resource
    private ClassMapper classMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private GradeMapper gradeMapper;
    @Resource
    private MajorMapper majorMapper;
    @Resource
    private TeacherMapper teacherMapper;
    @Resource
    private FacultyMapper facultyMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private TeachGroupMapper teachGroupMapper;

    @Resource
    private ScoreMapper scoreMapper;


    /**
     * 发送请求公共方法
     *
     * @param param
     * @return
     */
    private JSONArray sendPost(JSONObject param) {
        param.put("timestamp", System.currentTimeMillis());
        String secuKey = "8MD536A7";
        JSONObject headParam = new JSONObject();
        headParam.put("token", "001ZA1006JC0624SHCYL0627");
        headParam.put("version", "1.0");
        headParam.put("sign", getSign(param, secuKey));
        HttpJSONUtil jsonUtil = new HttpJSONUtil(ProjectConstants.API_URL_NEW);
        String json = jsonUtil.post(param.toString(), headParam);
        JSONObject jsonObject = JSONObject.parseObject(json);
        if (jsonObject.getIntValue("code") == 200) {
            try {
                JSONObject data = jsonObject.getJSONObject("data");
                String list = data.getString("list");
                JSONArray jsonArray = JSON.parseArray(list);
                return jsonArray;
            } catch (Exception e) {
            }
        }
        return null;
    }

    /**
     * 根据请求参数获取签名
     *
     * @param param
     * @return
     */
    private String getSign(JSONObject param, String secuKey) {
        if (param != null) {
            String[] sortedKeys = param.keySet().toArray(new String[]{});
            Arrays.sort(sortedKeys);
            StringBuffer str = new StringBuffer();
            for (String key : sortedKeys) {
                str.append(key).append("=").append(param.getString(key)).append("&");
            }
            return MD5.getMD5Str(str.toString().substring(0, str.toString().length() - 1) + secuKey);
        }
        return "";
    }


    @Override
    public void synchronizeDept() {
        logger.info("同步部门开始");
        int addTotal = 0;
        int updateTotal = 0;
        int deleteTotal = 0;

        JSONObject param = new JSONObject();
        param.put("action", "zzxx.org.find");
        param.put("pageNum", 1);
        param.put("pageSize", 200);
        //1部门；2教研组
        param.put("jglb", 1);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                String deptName = item.getString("jgmc");
                if (MicrovanUtil.isNotEmpty(deptName)) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("deptName", deptName);
                    Dept dept = deptMapper.checkDept(map);
                    if (dept == null) {
                        dept = new Dept();
                        dept.setDeptName(deptName);
                    }
                    //1有效 机构有效标识
                    String flag = item.getString("jgyxbs");
                    if ("1".equals(flag)) {
                        if (dept.getOid() == null) {
                            int line = deptMapper.insertSelective(dept);
                            if (line > 0) {
                                addTotal++;
                            }
                        } else {
                            int line = deptMapper.updateByPrimaryKeySelective(dept);
                            if (line > 0) {
                                updateTotal++;
                            }
                        }
                    } else if (dept.getOid() != null) {
                        int line = deptMapper.deleteByPrimaryKey(dept.getOid());
                        if (line > 0) {
                            deleteTotal++;
                        }
                    }
                }
            }
        }
        logger.info("同步部门结束：新增成功" + addTotal + "个，更新" + updateTotal + "个，删除" + deleteTotal + "个");
    }

    @Override
    public void synchronizeTeacher() {
        logger.info("同步教师开始");
        JSONObject param = new JSONObject();
        param.put("action", "zzjg.info.find");
        param.put("pageNum", 1);
        param.put("pageSize", 2000);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            JSONObject object = handleTeacherData(jsonArray);
            logger.info("同步教师结束：新增成功" + object.getIntValue("addTotal") + "个，更新" + object.getIntValue("updateTotal") + "个，删除" + object.getIntValue("deleteTotal") + "个");
        }
    }

    /**
     * 处理同步出来的教师数据
     * @param jsonArray
     */
    private JSONObject handleTeacherData(JSONArray jsonArray) {
        int addTotal = 0;
        int updateTotal = 0;
        int deleteTotal = 0;
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = (JSONObject) jsonArray.get(i);
            //工号
            String jobNo = item.getString("gh");
            if (MicrovanUtil.isNotEmpty(jobNo) && !"admin".equals(jobNo)) {
                Teacher teacher = teacherMapper.findTeacherByJobNo(jobNo);
                if (teacher == null) {
                    teacher = new Teacher();
                    teacher.setJobNo(jobNo);
                }
                //姓名
                String name = item.getString("xm");
                if (MicrovanUtil.isNotEmpty(name)) {
                    teacher.setName(name);
                }
                //性别
                String sex = item.getString("xbmStr");
                if (MicrovanUtil.isNotEmpty(sex)) {
                    int intSex = ProjectUtil.getSex(sex);
                    teacher.setSex(intSex);
                }
                //系部
//                String facultyName = item.getString("dep");
//                if (MicrovanUtil.isNotEmpty(facultyName)) {
//                    Faculty faculty = facultyMapper.findFacultyByName(facultyName);
//                    if (faculty != null) {
//                        teacher.setFacultyId(faculty.getOid());
//                    }
//                } else {
//                    teacher.setFacultyId(null);
//                }
                //教研组名称
                String teachGroup = item.getString("groupMc");
                if (MicrovanUtil.isNotEmpty(teachGroup)) {
                    TeachGroup tg = teachGroupMapper.findTeachGroupByName(teachGroup);
                    if (tg != null) {
                        teacher.setTeachGroup(tg.getOid());
                        teacher.setFacultyId(tg.getFacultyId());
                    }
                } else {
                    teacher.setTeachGroup(null);
                    teacher.setFacultyId(null);
                }
                //联系方式
                String phone = item.getString("yddh");
                if (MicrovanUtil.isNotEmpty(phone)) {
                    teacher.setPhone(phone);
                }
                //身份证号
                String idCard = item.getString("sfzjh");
                if (MicrovanUtil.isNotEmpty(idCard)) {
                    teacher.setIdCard(idCard);
                }
                //地址，此字段无内容
                String address = item.getString("txdz");
                if (MicrovanUtil.isNotEmpty(address)) {
                    teacher.setAddress(address);
                }
                //状态
                String stateStr = item.getString("dqztm");
                int state = 0;
                if (MicrovanUtil.isNotEmpty(stateStr)) {
                    state = Integer.valueOf(stateStr).intValue();
                    if (state == 11) {
                        teacher.setState(1);
                    } else if (state == 4) {
                        teacher.setState(2);
                    }
                }
                //教职工类别
                String type = item.getString("jzglbmStr");
                if (MicrovanUtil.isNotEmpty(type)) {
                    teacher.setType(type);
                }
                //只保留在职和返聘状态的老师
                if (state == 11 || state == 4) {

                    String typeCode = item.getString("jzglbm");
                    Integer teacherType = null;
                    //在职在编：专任教师 11 行政人员 13 工勤人员 14（在职）
                    //编外运行：编外运行 17（在职）
                    //行政外聘：外聘行政 15 （在职+返聘）
                    if ("11".equals(state) && (
                            "11".equals(typeCode) ||
                                    "13".equals(typeCode) ||
                                    "14".equals(typeCode))) {
                        teacherType = ProjectConstants.TEACHER_TYPE_ZZZB;
                    }
                    if ("11".equals(state) && "17".equals(typeCode)) {
                        teacherType = ProjectConstants.TEACHER_TYPE_BWYX;
                    }
                    if (("11".equals(state) || "04".equals(state)) && "15".equals(typeCode)) {
                        teacherType = ProjectConstants.TEACHER_TYPE_XZWP;
                    }

                    if (teacher.getId() == null) {
                        teacher.setIsValid(1);
                        if (teacher.getSex() != null) {
                            if (teacher.getSex().intValue() == 1) {
                                teacher.setPhotoPath("teacherPhoto/defaultman.png");
                            } else {
                                teacher.setPhotoPath("teacherPhoto/defaultwoman.png");
                            }
                        }
                        if (state == 11) {
                            teacher.setInvigilate(1);
                        } else if (state == 4) {
                            teacher.setInvigilate(2);
                        }
                        int line = teacherMapper.insertSelective(teacher);
                        if (line > 0) {
                            this.addOrUpdateUser(item, jobNo, teacher, teacherType);
                            addTotal++;
                        }
                    } else {
                        teacherMapper.updateByPrimaryKey(teacher);
                        this.addOrUpdateUser(item, jobNo, teacher, teacherType);
                        updateTotal++;
                    }
                } else {
                    //删除其他状态的教师
                    if (teacher != null && teacher.getId() != null) {
                        try {
                            teacherMapper.deleteByPrimaryKey(teacher.getId());
                            Map<String, Object> map = new HashMap<>();
                            map.put("loginName", jobNo);
                            User user = userMapper.checkUser(map);
                            if (user != null && user.getOid() != null) {
                                userMapper.deleteByPrimaryKey(user.getOid());
                                userRoleMapper.clearUserRole(user.getOid());
                            }
                        } catch (Exception e) {
                            logger.info(e.getMessage());
                            logger.info("teacherId=" + teacher.getId());
                            logger.info("teacherName=" + teacher.getName());
                        }
                        deleteTotal++;
                    }
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("addTotal", addTotal);
        jsonObject.put("updateTotal", updateTotal);
        jsonObject.put("deleteTotal", deleteTotal);
        return jsonObject;
    }

    private void addOrUpdateUser(JSONObject item, String jobNo, Teacher teacher, Integer teacherType) {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", jobNo);
        User user = userMapper.checkUser(map);
        if (user == null || (user != null && user.getOid() == null)) {
            //添加教师角色的用户
            user = new User();
            user.setLoginName(teacher.getJobNo());
            user.setUserName(teacher.getName());
            user.setState(ProjectConstants.USER_STATE_COMMON);
            /*String password = MD5Utils.GetMD5Code(ProjectConstants.DEFAULT_PASSWORD + ProjectConstants.TEAM_SIGN);
            user.setPassword(PasswordUtil.generate(password));*/
            user.setPassword(PasswordUtil.generate(ProjectConstants.DEFAULT_PASSWORD));
            //部门
            String deptName = item.getString("dep");
            if (MicrovanUtil.isNotEmpty(deptName)) {
                Map<String, Object> param = new HashMap<>();
                param.put("deptName", deptName);
                Dept dept = deptMapper.checkDept(param);
                if (MicrovanUtil.isNotEmpty(dept)) {
                    user.setDeptId(dept.getOid());
                }
            }
            user.setUserType(ProjectConstants.USER_TYPE_TEACHER);
            user.setTeacherType(teacherType);
            userMapper.insertSelective(user);

            Long roleId = ProjectConstants.ROLE_PTJS;
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getOid());
            userRole.setRoleId(roleId);
            userRoleMapper.insertSelective(userRole);
        } else {
            //部门
            String deptName = item.getString("dep");
            if (MicrovanUtil.isNotEmpty(deptName)) {
                Map<String, Object> param = new HashMap<>();
                param.put("deptName", deptName);
                Dept dept = deptMapper.checkDept(param);
                if (MicrovanUtil.isNotEmpty(dept)) {
                    user.setDeptId(dept.getOid());
                }
            }
            user.setUserType(ProjectConstants.USER_TYPE_TEACHER);
            user.setTeacherType(teacherType);
            user.setUserName(teacher.getName());
            userMapper.updateByPrimaryKeySelective(user);
        }
    }


    @Override
    public void synchronizeClass() {
        logger.error("同步班级开始");
        int addTotal = 0;
        int updateTotal = 0;

        JSONObject param = new JSONObject();
        param.put("action", "zzjx.class.find");
        param.put("pageNum", 1);
        param.put("pageSize", 2000);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                //班号
                String code = item.getString("xzbdm");
                if (MicrovanUtil.isNotEmpty(code)) {
                    Class clazz = classMapper.findClassByCode(code);
                    if (clazz == null) {
                        clazz = new Class();
                        clazz.setCode(code);
                    }
                    //班级名称
                    String name = item.getString("xzbmc");
                    if (MicrovanUtil.isNotEmpty(name)) {
                        clazz.setName(name);
                    }
                    //年级
                    String njdm = item.getString("njdm");
                    String gradeName = getGradeNameFromCode(njdm);
                    if (MicrovanUtil.isNotEmpty(gradeName)) {
                        Grade checkGrade = new Grade();
                        checkGrade.setGradeName(gradeName);
                        Grade grade = gradeMapper.checkGrade(checkGrade);
                        if (grade != null) {
                            clazz.setGradeId(grade.getOid());
                        }
                    }
                    //班主任
                    String bzrgh = item.getString("bzrgh");
                    if (MicrovanUtil.isNotEmpty(bzrgh)) {
                        Teacher teacher = teacherMapper.findTeacherByJobNo(bzrgh);
                        if (teacher != null) {
                            clazz.setTeacherId(teacher.getId());
                        } else {
                            clazz.setTeacherId(null);
                        }
                    }

                    clazz.setCount(0);
                    //获取专业代码,专业代码是专业数据中的专业编号
                    String zybh = item.getString("zydm");
                    JSONObject zy = getMajorFromCode(zybh);
                    if (MicrovanUtil.isNotEmpty(zy)) {
                        //专业名称
                        String zymc = zy.getString("zymc");
                        Major major = majorMapper.findMajorByName(zymc);
                        if (major != null) {
                            clazz.setMajorId(major.getOid());
                        }

                        //系部名称
                        String xbmc = zy.getString("ksjgh");
                        if (MicrovanUtil.isNotEmpty(xbmc)) {
                            Faculty faculty = facultyMapper.findFacultyByName(xbmc);
                            if (faculty != null) {
                                clazz.setFacultyId(faculty.getOid());
                            }
                        }
                    }

                    //是否有效，新接口不存在这个字段，默认都有效
                    clazz.setIsValid(1);
                    //执行插入或修改
                    if (clazz.getId() == null) {
                        clazz.setJoinExam(1);
                        int line = classMapper.insertSelective(clazz);
                        if (line > 0) {
                            addTotal++;
                        }
                    } else {
                        classMapper.updateByPrimaryKeySelective(clazz);
                        updateTotal++;
                    }
                }
            }
        }
        logger.error("同步班级结束：新增成功" + addTotal + "个，更新" + updateTotal + "个");
    }

    /**
     * 根据年级代码获取年级名称
     * @param njdm
     * @return
     */
    private String getGradeNameFromCode(String njdm) {
        JSONObject param = new JSONObject();
        param.put("action", "zzjx.grade.find");
        param.put("pageNum", 1);
        param.put("njdm", njdm);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            JSONObject item = (JSONObject) jsonArray.get(0);
            if (item != null) {
                return item.getString("njmc");
            }
        }
        return "";
    }

    /**
     * 根据专业编号获取专业
     * @param zybh
     * @return
     */
    private JSONObject getMajorFromCode(String zybh) {
        JSONObject param = new JSONObject();
        param.put("action", "zzjx.info.find");
        param.put("pageNum", 1);
        param.put("zybh", zybh);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            JSONObject item = (JSONObject) jsonArray.get(0);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    @Override
    public void synchronizeStudent() {
        logger.error("同步学生开始");
        JSONObject re = new JSONObject();
        re.put("addTotal", 0);
        re.put("updateTotal", 0);

        JSONObject param = new JSONObject();
        param.put("action", "zzxs.info.find");
        param.put("pageNum", 1);
        param.put("pageSize", 2000);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            handleStudentData(jsonArray, re);
            while (jsonArray.size() == 2000) {
                param.put("pageNum", param.getIntValue("pageNum") + 1);
                jsonArray = this.sendPost(param);
                if (MicrovanUtil.isNotEmpty(jsonArray)) {
                    handleStudentData(jsonArray, re);
                }
            }
        }
        logger.error("同步学生结束：新增成功" + re.getIntValue("addTotal") + "个，更新" + re.getIntValue("updateTotal") + "个");
    }

    /**
     * 处理同步出来的学生数据
     * @param jsonArray
     * @param re
     */
    private void handleStudentData(JSONArray jsonArray, JSONObject re) {
        logger.info("开始处理数据，数据条数="+jsonArray.size());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject item = (JSONObject) jsonArray.get(i);
            //学号
            String studentNo = item.getString("xh");
            if (MicrovanUtil.isNotEmpty(studentNo)) {
                Map<String, Object> map = new HashMap<>();
                map.put("studentNo", studentNo);
                Student student = studentMapper.selectByStudentNo(map);
                if (student == null) {
                    student = new Student();
                    student.setStudentNo(studentNo);
                }
                //班号
                String classNo = item.getString("szbh");
                if (MicrovanUtil.isNotEmpty(classNo)) {
                    Class clazz = classMapper.findClassByCode(classNo);
                    if (clazz != null) {
                        if (MicrovanUtil.isNotEmpty(clazz.getGradeId())) {
                            Grade grade = gradeMapper.selectByPrimaryKey(clazz.getGradeId());
                            if (grade != null && Integer.parseInt(grade.getYear()) >= 2015) {
                                student.setClassId(clazz.getId());
                                student.setGradeId(grade.getOid());
                                //专业
                                String majorName = item.getString("zymc");
                                if (MicrovanUtil.isNotEmpty(majorName)) {
                                    Major major = majorMapper.findMajorByName(majorName);
                                    if (major != null) {
                                        student.setMajorId(major.getOid());
                                    }
                                }
                                //姓名
                                String name = item.getString("xm");
                                if (MicrovanUtil.isNotEmpty(name)) {
                                    student.setName(name);
                                }
                                //身份证号
                                String idCard = item.getString("sfzjh");
                                if (MicrovanUtil.isNotEmpty(idCard)) {
                                    student.setIdCard(idCard);
                                }
                                //性别
                                String sex = item.getString("xbmStr");
                                if (MicrovanUtil.isNotEmpty(sex)) {
                                    int intSex = ProjectUtil.getSex(sex);
                                    student.setSex(intSex);
                                }
                                //民族
                                String nation = item.getString("mzmStr");
                                if (MicrovanUtil.isNotEmpty(nation)) {
                                    student.setNation(nation);
                                }
                                //联系方式
                                String phone = item.getString("xslxdh");
                                if (MicrovanUtil.isNotEmpty(phone)) {
                                    student.setPhone(phone);
                                }
                                //状态
                                String status = item.getString("xsdqztm");
                                int state;
                                if (MicrovanUtil.isNotEmpty(status)) {
                                    if ("01".equals(status) || "05".equals(status)) {
                                        state = 1;
                                    } else if ("07".equals(status)) {
                                        state = 0;
                                    } else {
                                        state = 2;
                                    }
                                    student.setStatus(state);
                                }
                                if (student.getId() == null) {
                                    student.setIsValid(1);
                                    int line = studentMapper.insertSelective(student);
                                    if (line > 0) {
                                        //更新班级人数
                                        clazz.setCount(clazz.getCount() + 1);
                                        classMapper.updateByPrimaryKeySelective(clazz);

                                        //添加学生用户及角色
                                        User user = new User();
                                        user.setLoginName(student.getStudentNo());
                                        user.setUserName(student.getName());
                                        user.setState(ProjectConstants.USER_STATE_COMMON);
                                        user.setUserType(ProjectConstants.USER_TYPE_STU);
                                        String birthday = TeachEvaUtil.convertBirthdayFromIdcard(idCard);
                                        String password = ProjectConstants.DEFAULT_PASSWORD;
                                        if (MicrovanUtil.isNotEmpty(birthday)) {
                                            password = birthday;
                                        }
                                        //password = MD5Utils.GetMD5Code(password + ProjectConstants.TEAM_SIGN);
                                        user.setPassword(PasswordUtil.generate(password));
                                        userMapper.insertSelective(user);

                                        UserRole userRole = new UserRole();
                                        userRole.setUserId(user.getOid());
                                        userRole.setRoleId(ProjectConstants.ROLE_STU); //学生
                                        userRoleMapper.insertSelective(userRole);
                                        re.put("addTotal", re.getIntValue("addTotal") + 1);
                                    }
                                } else {
                                    studentMapper.updateByPrimaryKeySelective(student);
                                    Map<String, Object> stuMap = new HashMap<>(5);
                                    stuMap.put("loginName", studentNo);
                                    User user = userMapper.checkUser(stuMap);
                                    if (user != null && user.getOid() != null) {
                                        if (MicrovanUtil.isNotEmpty(idCard)) {
                                            String birthday = TeachEvaUtil.convertBirthdayFromIdcard(idCard);
                                            String password = ProjectConstants.DEFAULT_PASSWORD;
                                            if (MicrovanUtil.isNotEmpty(birthday)) {
                                                password = birthday;
                                            }
                                            //password = MD5Utils.GetMD5Code(password + ProjectConstants.TEAM_SIGN);
                                            user.setPassword(PasswordUtil.generate(password));
                                            user.setUserName(student.getName());
                                            userMapper.updateByPrimaryKeySelective(user);
                                        }
                                    } else {
                                        //添加学生用户及角色
                                        user = new User();
                                        user.setLoginName(student.getStudentNo());
                                        user.setUserName(student.getName());
                                        user.setState(ProjectConstants.USER_STATE_COMMON);
                                        user.setUserType(ProjectConstants.USER_TYPE_STU);
                                        String birthday = TeachEvaUtil.convertBirthdayFromIdcard(idCard);
                                        String password = ProjectConstants.DEFAULT_PASSWORD;
                                        if (MicrovanUtil.isNotEmpty(birthday)) {
                                            password = birthday;
                                        }
                                        //password = MD5Utils.GetMD5Code(password + ProjectConstants.TEAM_SIGN);
                                        user.setPassword(PasswordUtil.generate(password));
                                        userMapper.insertSelective(user);

                                        UserRole userRole = new UserRole();
                                        userRole.setUserId(user.getOid());
                                        userRole.setRoleId(ProjectConstants.ROLE_STU); //学生
                                        userRoleMapper.insertSelective(userRole);
                                    }
                                    re.put("updateTotal", re.getIntValue("updateTotal") + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void synchronizeTerm() {
        logger.error("同步学期开始");
        int addTotal = 0;
        int updateTotal = 0;

        JSONObject param = new JSONObject();
        param.put("action", "zzjx.semester.find");
        param.put("pageNum", 1);
        param.put("pageSize", 500);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                //学期名称
                String termName = item.getString("xqmc");
                if (MicrovanUtil.isNotEmpty(termName)) {
                    int termNo = 1;
                    if (termName.contains("第二")) {
                        termNo = 2;
                    }
                    Term checkTerm = new Term();
                    checkTerm.setName(termName);
                    checkTerm.setTermNo(termNo);
                    Term term = termMapper.checkTerm(checkTerm);
                    if (term == null) {
                        term = new Term();
                        term.setName(termName);
                        term.setTermNo(checkTerm.getTermNo());
                    }
                    //开始日期
                    String beginTime = item.getString("xqksrq");
                    if (MicrovanUtil.isNotEmpty(beginTime)) {
                        term.setBeginTime(MicrovanUtil.formatDate("yyyy-MM-dd", beginTime));
                    }
                    //结束日期
                    String endTime = item.getString("xqjsrq");
                    if (MicrovanUtil.isNotEmpty(endTime)) {
                        term.setEndTime(MicrovanUtil.formatDate("yyyy-MM-dd", endTime));
                    }
                    String year = item.getString("xn");
                    if (MicrovanUtil.isNotEmpty(year)) {
                        term.setYear(year);
                    }
                    if (term.getId() == null) {
                        term.setIsValid(1);
                        int line = termMapper.insertSelective(term);
                        if (line > 0) {
                            addTotal++;
                        }
                    } else {
                        termMapper.updateByPrimaryKeySelective(term);
                        updateTotal++;
                    }
                }
            }
        }
        logger.error("同步学期结束：新增成功" + addTotal + "个，更新" + updateTotal + "个");
    }

    @Override
    public void synchronizeGrade() {
        logger.error("同步年级开始");
        int addTotal = 0;
        int updateTotal = 0;

        JSONObject param = new JSONObject();
        param.put("action", "zzjx.grade.find");
        param.put("pageNum", 1);
        param.put("pageSize", 500);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                //年级名称
                String gradeName = item.getString("njmc");
                if (MicrovanUtil.isNotEmpty(gradeName)) {
                    Grade checkGrade = new Grade();
                    checkGrade.setGradeName(gradeName);
                    Grade grade = gradeMapper.checkGrade(checkGrade);
                    if (grade == null) {
                        grade = new Grade();
                        grade.setGradeName(gradeName);
                    }
                    String year = item.getString("ssnf");
                    if (MicrovanUtil.isNotEmpty(year)) {
                        grade.setYear(year);
                    }
                    if (grade.getOid() == null) {
                        int line = gradeMapper.insertSelective(grade);
                        if (line > 0) {
                            addTotal++;
                        }
                    } else {
                        gradeMapper.updateByPrimaryKeySelective(grade);
                        updateTotal++;
                    }
                }
            }
        }
        logger.error("同步年级结束：新增成功" + addTotal + "个，更新" + updateTotal + "个");
    }

    @Override
    public void synchronizeMajor() {
        logger.error("同步专业开始");
        int addTotal = 0;
        int updateTotal = 0;

        JSONObject param = new JSONObject();
        param.put("action", "zzjx.info.find");
        param.put("pageNum", 1);
        param.put("pageSize", 2000);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                //专业名称
                String majorName = item.getString("zymc");
                if (MicrovanUtil.isNotEmpty(majorName)) {
                    Major checkMajor = new Major();
                    checkMajor.setMajorName(majorName);
                    Major major = majorMapper.checkMajor(checkMajor);
                    if (major == null) {
                        major = new Major();
                        major.setMajorName(majorName);
                    }
                    String majorCode = item.getString("zybh");
                    if (MicrovanUtil.isNotEmpty(majorCode)) {
                        major.setMajorCode(majorCode);
                    }
                    //院系名称
                    String facultyName = item.getString("ksjgh");
                    if (MicrovanUtil.isNotEmpty(facultyName)) {
                        Faculty faculty = facultyMapper.findFacultyByName(facultyName);
                        if (faculty != null) {
                            major.setFacultyId(faculty.getOid());
                        }
                    }
                    String year = item.getString("jlny");
                    if (MicrovanUtil.isNotEmpty(year)) {
                        year = year.split("-")[0];
                        major.setSystem(year);
                    }
                    if (major.getOid() == null) {
                        major.setState(1);
                        int line = majorMapper.insertSelective(major);
                        if (line > 0) {
                            addTotal++;
                        }
                    } else {
                        majorMapper.updateByPrimaryKeySelective(major);
                        updateTotal++;
                    }
                }
            }
        }
        logger.error("同步专业结束：新增成功" + addTotal + "个，更新" + updateTotal + "个");
    }

    @Override
    public void synchronizeFaculty() {
        logger.error("同步系部开始");
        int addTotal = 0;
        int updateTotal = 0;
        int delTotal = 0;

        JSONObject param = new JSONObject();
        param.put("action", "zzxx.org.find");
        param.put("pageNum", 1);
        param.put("pageSize", 200);
        //1系部；2教研组
        param.put("jglb", 1);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                String facultyName = item.getString("jgmc");
                if (MicrovanUtil.isNotEmpty(facultyName) && facultyName.contains("系")) {
                    Faculty checkFaculty = new Faculty();
                    checkFaculty.setFacultyName(facultyName);
                    Faculty faculty = facultyMapper.checkFaculty(checkFaculty);
                    if (faculty == null) {
                        faculty = new Faculty();
                        faculty.setFacultyName(facultyName);
                    }
                    String jobNo = item.getString("fzrh");
                    if (MicrovanUtil.isNotEmpty(jobNo)) {
                        Teacher teacher = teacherMapper.findTeacherByJobNo(jobNo);
                        if (teacher != null) {
                            faculty.setFacultyAdmin(teacher.getId());
                        }
                    }
                    if (faculty.getOid() == null) {
                        faculty.setCreateTime(new Date());
                        int line = facultyMapper.insertSelective(faculty);
                        if (line > 0) {
                            addTotal++;
                        }
                    } else {
                        facultyMapper.updateByPrimaryKeySelective(faculty);
                        updateTotal++;
                    }
                } else {
                    Faculty checkFaculty = new Faculty();
                    checkFaculty.setFacultyName(facultyName);
                    Faculty faculty = facultyMapper.checkFaculty(checkFaculty);
                    if (MicrovanUtil.isNotEmpty(faculty)) {
                        facultyMapper.deleteByPrimaryKey(faculty.getOid());
                        delTotal++;
                    }
                }
            }
        }
        logger.error("同步系部结束：新增成功" + addTotal + "个，更新" + updateTotal + "个，删除" + delTotal + "个");
    }


    @Override
    public void synchronizeTeachGroup() {
        logger.error("同步教研组开始");
        int addTotal = 0;
        int updateTotal = 0;

        JSONObject param = new JSONObject();
        param.put("action", "zzxx.org.find");
        param.put("pageNum", 1);
        param.put("pageSize", 2000);
        param.put("jgyxbs", "1"); //只同步有效的教研组
        //1系部；2教研组
        param.put("jglb", 2);
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = (JSONObject) jsonArray.get(i);
                String groupName = item.getString("jgmc");
                if (MicrovanUtil.isNotEmpty(groupName)) {
                    TeachGroup checkTeachGroup = new TeachGroup();
                    checkTeachGroup.setGroupName(groupName);
                    TeachGroup teachGroup = teachGroupMapper.checkTeachGroup(checkTeachGroup);
                    if (teachGroup == null || (teachGroup != null && teachGroup.getOid() == null)) {
                        teachGroup = new TeachGroup();
                    }
                    teachGroup.setGroupName(groupName);
                    //负责人
                    String adminName = item.getString("fzrh");
                    if (MicrovanUtil.isNotEmpty(adminName)) {
                        Teacher teacher = teacherMapper.findTeacherByJobNo(adminName);
                        if (teacher != null) {
                            teachGroup.setTeacherId(teacher.getId());
                        }
                    }
                    //系部名称
                    String facultyName = item.getString("dssjjgh");
                    Faculty faculty = facultyMapper.findFacultyByName(facultyName);
                    if (faculty != null) {
                        teachGroup.setFacultyId(faculty.getOid());
                    }

                    if (teachGroup.getOid() == null) {
                        teachGroup.setCreateTime(new Date());
                        int line = teachGroupMapper.insertSelective(teachGroup);
                        if (line > 0) {
                            addTotal++;
                        }
                    } else {
                        teachGroupMapper.updateByPrimaryKeySelective(teachGroup);
                        updateTotal++;
                    }
                }
            }
        }
        logger.error("同步教研组结束：新增成功" + addTotal + "个，更新" + updateTotal + "个");
    }

    /*public static void main(String[] args) {
        JSONObject param = new JSONObject();
        param.put("action", "zzxx.org.find");
        param.put("pageNum", 1);
        param.put("pageSize", 200);
        //1部门；2教研组
        param.put("jglb", 1);
        long mi = System.currentTimeMillis();
        param.put("timestamp", mi);
        String secuKey = "8MD536A7";
        System.out.println(mi);
        System.out.println(getSign(param, secuKey));

    }*/

    @Override
    public String syncStuInfoInitStuScore(){
        StringBuilder sb = new StringBuilder();
        long startTimeMillis = DateUtil.current();
        sb.append("同步学生信息到得分信息表初始化")
                .append(" 开始时间：").append(DateUtil.now());
        Integer maxScoreStuId = scoreMapper.selectMaxStuId();
        maxScoreStuId = maxScoreStuId != null ? maxScoreStuId : 0;
        int num = scoreMapper.insertScoreFromStu(maxScoreStuId);
        sb.append(StrUtil.format(" maxScoreStuId:{}, 同步插入学生信息 num:{}", maxScoreStuId, num));
        sb.append(" 结束时间：").append(DateUtil.now())
                .append(" 花费时间：")
                .append(DateUtil.formatBetween(DateUtil.current() - startTimeMillis, BetweenFormatter.Level.SECOND));
        logger.info(sb.toString());
        return sb.toString();
    }
}
