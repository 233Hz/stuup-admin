package com.poho.stuup.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.poho.common.constant.CommonConstants;
import com.poho.common.util.MD5;
import com.poho.common.util.MD5Utils;
import com.poho.common.util.MicrovanUtil;
import com.poho.common.util.PasswordUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.dao.DeptMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.dao.UserRoleMapper;
import com.poho.stuup.model.Dept;
import com.poho.stuup.model.User;
import com.poho.stuup.model.UserRole;
import com.poho.stuup.service.ISynchronizeService;
import com.poho.stuup.util.HttpJSONUtil;
import com.poho.stuup.util.ProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

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
    private UserMapper userMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

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
                            dept.setCreateTime(new Date());
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
     * 同步学历
     *
     * @return
     */
    private String synchronizeDegree(String gh) {
        String degree = "";
        JSONObject param = new JSONObject();
        param.put("action", "zzjg.academic.degree.find");
        param.put("gh", gh);
        param.put("orderBy", "xlm");
        param.put("solt", "asc");
        JSONArray jsonArray = this.sendPost(param);
        if (MicrovanUtil.isNotEmpty(jsonArray)) {
            JSONObject item = (JSONObject) jsonArray.get(0);
            degree = ProjectUtil.convertDegree(item.getString("xlm"));
        }
        return degree;
    }

    /**
     * 处理同步出来的教师数据
     *
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
            if (MicrovanUtil.isNotEmpty(jobNo)) {
                Map<String, Object> map = new HashMap<>();
                map.put("loginName", jobNo);
                User user = userMapper.checkUser(map);
                if (user == null) {
                    user = new User();
                    user.setLoginName(jobNo);
                }
                //姓名
                String name = item.getString("xm");
                if (MicrovanUtil.isNotEmpty(name)) {
                    user.setUserName(name);
                }
                //性别
                String sex = item.getString("xbmStr");
                if (MicrovanUtil.isNotEmpty(sex)) {
                    int intSex = ProjectUtil.getSex(sex);
                    user.setSex(intSex);
                }
                //联系方式
                String phone = item.getString("yddh");
                if (MicrovanUtil.isNotEmpty(phone)) {
                    user.setMobile(phone);
                }
                //身份证号
                String idCard = item.getString("sfzjh");
                if (MicrovanUtil.isNotEmpty(idCard)) {
                    user.setIdCard(idCard);
                    //生日
                    user.setBirthday(ProjectUtil.obtainBirthdayFromIdCard(idCard));
                }
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
                //学历
                String degree = synchronizeDegree(jobNo);
                if (MicrovanUtil.isNotEmpty(degree)) {
                    user.setDegree(degree);
                }
                //教师状态
                String state = item.getString("dqztm");
                // 11在职，04返聘
                if ("11".equals(state) || "04".equals(state)) {
                    String typeCode = item.getString("jzglbm");
                    //在职在编：专任教师 11 行政人员 13 工勤人员 14（在职）
                    //编外运行：编外运行 17（在职）
                    //行政外聘：外聘行政 15 （在职+返聘）
                    boolean via = false;
                    if ("11".equals(state) && (
                            "11".equals(typeCode) ||
                                    "13".equals(typeCode) ||
                                    "14".equals(typeCode))) {
                        user.setUserType(ProjectConstants.USER_TYPE_ZZZB);
                        via = true;
                    }
                    if ("11".equals(state) && "17".equals(typeCode)) {
                        user.setUserType(ProjectConstants.USER_TYPE_BWYX);
                        via = true;
                    }
                    if (("11".equals(state) || "04".equals(state)) && "15".equals(typeCode)) {
                        user.setUserType(ProjectConstants.USER_TYPE_XZWP);
                        via = true;
                    }
                    if (via) {
                        if (user.getOid() == null) {
                            user.setState(ProjectConstants.USER_STATE_COMMON);
                            String password = MD5Utils.GetMD5Code(ProjectConstants.DEFAULT_PASSWORD + ProjectConstants.TEAM_SIGN);
                            user.setPassword(PasswordUtil.generate(password));
                            user.setCreateTime(new Date());
                            int line = userMapper.insertSelective(user);
                            if (line > 0) {
                                //判断是否有普通教师角色，没有的话加一个
                                Map<String, Object> param = new HashMap<>();
                                param.put("userId", user.getOid());
                                List<Long> roleIds = userRoleMapper.queryUserYearRoles(param);
                                Long roleId = ProjectConstants.ROLE_PTJS;
                                if (MicrovanUtil.isEmpty(roleIds) || (MicrovanUtil.isNotEmpty(roleIds) && !roleIds.contains(roleId))) {
                                    UserRole userRole = new UserRole();
                                    userRole.setUserId(user.getOid());
                                    userRole.setRoleId(roleId);
                                    userRole.setCreateTime(new Date());
                                    userRoleMapper.insertSelective(userRole);
                                }
                                addTotal++;
                            }
                        } else {
                            int line = userMapper.updateByPrimaryKeySelective(user);
                            if (line > 0) {
                                updateTotal++;
                            }
                        }
                    } else if (user.getOid() != null) {
                        User uObj = new User();
                        uObj.setOid(user.getOid());
                        uObj.setState(CommonConstants.USER_FORBIDDEN_STATE);
                        //int line = userMapper.deleteByPrimaryKey(user.getOid());
                        //数据同步过来数据，不能直接物理删除，只能设置为禁用状态
                        int line = userMapper.updateByPrimaryKeySelective(uObj);
                        if (line > 0) {
                            userRoleMapper.clearUserAllRole(user.getOid());
                            deleteTotal++;
                        }
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
}
