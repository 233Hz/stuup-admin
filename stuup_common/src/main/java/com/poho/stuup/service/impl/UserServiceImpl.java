package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.*;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusAssessTeacher;
import com.poho.stuup.custom.CusAssessUser;
import com.poho.stuup.custom.CusNormScore;
import com.poho.stuup.custom.CusUser;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author wupeng
 * @Description 用户处理实现类
 * @Date 2020-07-22 21:33
 * @return
 */
@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private MsgMapper msgMapper;
    @Resource
    private YearMapper yearMapper;
    @Resource
    private DeptMapper deptMapper;
    @Resource
    private StandardCategoryMapper standardCategoryMapper;
    @Resource
    private StandardNormMapper standardNormMapper;
    @Resource
    private AssessRecordMapper assessRecordMapper;
    @Resource
    private AssessRangeMapper assessRangeMapper;
    @Resource
    private RegisterMapper registerMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return userMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(User record) {
        return userMapper.insert(record);
    }

    @Override
    public int insertSelective(User record) {
        return userMapper.insertSelective(record);
    }

    @Override
    public User selectByPrimaryKey(Long oid) {
        return userMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(User record) {
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(User record) {
        return userMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel checkLogin(String mobile, String msgCode, String msgId) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("登录失败，请稍后重试");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        User user = userMapper.checkUser(map);
        if (MicrovanUtil.isNotEmpty(user)) {
            if (user.getState().intValue() == ProjectConstants.USER_STATE_COMMON) {
                Year year = yearMapper.findCurrYear();
                if (MicrovanUtil.isNotEmpty(year)) {
                    map.put("yearId", year.getOid());
                }
                map.put("userId", user.getOid());
                AssessRange assessRange = assessRangeMapper.checkAssessRange(map);
                List<Long> roleIds = userRoleMapper.queryUserYearRoles(map);
                if (MicrovanUtil.isNotEmpty(roleIds)) {
                    Msg msg = msgMapper.selectByPrimaryKey(Long.valueOf(msgId));
                    if (MicrovanUtil.isNotEmpty(msg)) {
                        Date sendTime = msg.getSendTime();
                        Date nowTime = new Date();
                        long diff = nowTime.getTime() - sendTime.getTime();
                        if (diff > ProjectConstants.MSG_VALID_TIME) {
                            model.setCode(CommonConstants.CODE_EXCEPTION);
                            model.setMessage("验证码过期");
                        } else if (msg.getMsgCode().equals(msgCode)) {
                            CusUser cusUser = new CusUser();
                            cusUser.setUserId(user.getOid());
                            cusUser.setUserName(user.getUserName());
                            cusUser.setMobile(user.getMobile());
                            cusUser.setStaffType(user.getUserType());
                            if (MicrovanUtil.isNotEmpty(year)) {
                                cusUser.setYearId(year.getOid());
                                cusUser.setYearName(year.getYearName());
                            }
                            if (MicrovanUtil.isNotEmpty(assessRange)) {
                                cusUser.setDeptId(assessRange.getDeptId());
                                cusUser.setDeptName(assessRange.getDeptName());
                                cusUser.setUserRangeId(assessRange.getOid());
                            }
                            Register register = registerMapper.checkRegister(map);
                            if (MicrovanUtil.isNotEmpty(register)) {
                                cusUser.setPosition(register.getPosition());
                            }
                            cusUser.setRoleIds(ProjectUtil.splitListUseComma(roleIds));
                            model.setCode(CommonConstants.CODE_SUCCESS);
                            model.setMessage("登录成功");
                            model.setToken(JwtUtil.createOneDayJwt(user.getOid().toString()));
                            model.setData(cusUser);
                        } else {
                            model.setCode(CommonConstants.CODE_EXCEPTION);
                            model.setMessage("验证码错误");
                        }
                    } else {
                        model.setCode(CommonConstants.CODE_EXCEPTION);
                        model.setMessage("验证码错误");
                    }
                } else {
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                    model.setMessage("用户未设置角色，暂无法登录");
                }
            } else {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMessage("你的账号已被禁止登录系统");
            }
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("手机号码非本系统用户");
        }
        return model;
    }

    @Override
    public ResponseModel findDataPageResult(String key, String state, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        if (MicrovanUtil.isNotEmpty(state)) {
            map.put("state", Integer.valueOf(state));
        }
        int count = userMapper.queryTotal(map);
        PageData<User> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<User> list = userMapper.queryList(map);
        if (MicrovanUtil.isNotEmpty(list)) {
            Year year = yearMapper.findCurrYear();
            map.clear();
            map.put("yearId", year.getOid());
            for (User user : list) {
                map.put("userId", user.getOid());
                List<Long> roleIds = userRoleMapper.queryUserYearRoles(map);
                if (MicrovanUtil.isNotEmpty(roleIds)) {
                    List<String> roleNames = roleMapper.queryRoleNames(roleIds);
                    user.setRoles(roleIds);
                    user.setRoleNames(ProjectUtil.splitListUseComma(roleNames));
                }
            }
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
    public ResponseModel saveOrUpdate(User user) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("mobile", user.getMobile());
        if (MicrovanUtil.isNotEmpty(user.getOid())) {
            param.put("oid", user.getOid());
        }
        User checkUser = userMapper.checkUser(param);
        if (MicrovanUtil.isNotEmpty(checkUser)) {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("手机号码已存在");
            return model;
        } else {
            if (MicrovanUtil.isNotEmpty(user.getOid())) {
                line = userMapper.updateByPrimaryKeySelective(user);
            } else {
                user.setCreateTime(new Date());
                String password = MD5Utils.GetMD5Code(ProjectConstants.DEFAULT_PASSWORD + ProjectConstants.TEAM_SIGN);
                user.setPassword(PasswordUtil.generate(password));
                line = userMapper.insertSelective(user);
            }
        }
        if (line > 0) {
            if (MicrovanUtil.isNotEmpty(user.getRoles())) {
                //修改用户信息时，清除用户角色只会清除不带year_id角色
                userRoleMapper.clearUserRole(user.getOid());
                for (Long roleId : user.getRoles()) {
                    if (roleId > ProjectConstants.ROLE_PTJS) {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(user.getOid());
                        userRole.setRoleId(roleId);
                        userRole.setCreateTime(new Date());
                        userRoleMapper.insertSelective(userRole);
                    }
                }
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("保存失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel del(String ids) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("删除失败，请稍后重试");
        String[] idArr = ids.split(",");
        if (MicrovanUtil.isNotEmpty(idArr)) {
            int line = userMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel queryList() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<User> users = userMapper.queryList(null);
        if (MicrovanUtil.isNotEmpty(users)) {
            List<CusMap> data = new ArrayList<>();
            for (User user : users) {
                CusMap map = new CusMap(user.getOid(), user.getUserName());
                data.add(map);
            }
            model.setData(data);
        }
        return model;
    }

    @Override
    public ResponseModel updatePassword(String userId, Map params) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("修改失败，请稍后重试");
        String opassword = params.get("opassword").toString();
        String npassword = params.get("npassword").toString();
        String rpassword = params.get("rpassword").toString();
        if (MicrovanUtil.isNotEmpty(userId)) {
            User user = userMapper.selectByPrimaryKey(Long.valueOf(userId));
            if (MicrovanUtil.isNotEmpty(user)) {
                if (PasswordUtil.verify(opassword, user.getPassword())) {
                    if (npassword.equals(rpassword)) {
                        user.setPassword(PasswordUtil.generate(npassword));
                        int line = userMapper.updateByPrimaryKeySelective(user);
                        if (line > 0) {
                            model.setCode(CommonConstants.CODE_SUCCESS);
                            model.setMessage("修改成功，请重新登录");
                        }
                    } else {
                        model.setCode(CommonConstants.CODE_EXCEPTION);
                        model.setMessage("两次新密码输入不一致");
                    }
                } else {
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                    model.setMessage("旧密码错误，请重新输入");
                }
            }
        }
        return model;
    }

    @Override
    public ResponseModel findAssessMiddleUsers(Long yearId, Long deptId, Integer assessType, Long assessUserId) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<CusAssessUser> assessUsers = new ArrayList<>();
        User findUser = userMapper.selectByPrimaryKey(assessUserId);
        if (MicrovanUtil.isNotEmpty(findUser)) {
            Map<String, Object> param = new HashMap<>();
            param.put("yearId", yearId);
            //分管领导或群众评中层时只查询他所在部门下的中层
            if (assessType.intValue() == ProjectConstants.ASSESS_TYPE_FGLDPZC || assessType.intValue() == ProjectConstants.ASSESS_TYPE_QZPZC) {
                param.put("deptId", deptId);
            }
            List<User> users = userMapper.findAssessMiddleUsers(param);
            if (MicrovanUtil.isNotEmpty(users)) {
                for (User user : users) {
                    CusAssessUser assessUser = new CusAssessUser();
                    assessUser.setUserId(user.getOid());
                    assessUser.setUserName(user.getUserName());
                    List<CusNormScore> scores = new ArrayList<>();
                    List<StandardCategory> list = standardCategoryMapper.queryList(null);
                    if (MicrovanUtil.isNotEmpty(list)) {
                        int total = 0;
                        for (StandardCategory category : list) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("categoryId", Long.valueOf(category.getOid()));
                            List<StandardNorm> standardNorms = standardNormMapper.queryList(map);
                            for (StandardNorm standardNorm: standardNorms) {
                                CusNormScore normScore = new CusNormScore();
                                normScore.setNormId(standardNorm.getOid());
                                Map<String, Object> recordParam = new HashMap<>();
                                recordParam.put("yearId", yearId);
                                recordParam.put("userId", user.getOid());
                                recordParam.put("assessType", assessType);
                                recordParam.put("assessUser", assessUserId);
                                recordParam.put("normId", standardNorm.getOid());
                                AssessRecord assessRecord = assessRecordMapper.checkAssessRecord(recordParam);
                                if (MicrovanUtil.isNotEmpty(assessRecord)) {
                                    normScore.setScore(assessRecord.getScore());
                                } else {
                                    normScore.setScore(0);
                                }
                                total = total + normScore.getScore();
                                scores.add(normScore);
                            }
                        }
                        CusNormScore cusNormScore = new CusNormScore();
                        cusNormScore.setNormId(ProjectConstants.TEMP_COUNT_ID);
                        cusNormScore.setScore(total);
                        scores.add(cusNormScore);
                    }
                    assessUser.setScores(scores);
                    assessUsers.add(assessUser);
                }
            }
        }
        model.setData(assessUsers);
        return model;
    }

    @Override
    public ResponseModel findAssessRangeMiddleUsers(Long userRangeId) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<CusAssessUser> assessUsers = new ArrayList<>();
        AssessRange assessRange = assessRangeMapper.selectByPrimaryKey(userRangeId);
        if (MicrovanUtil.isNotEmpty(assessRange)) {
            Map<String, Object> param = new HashMap<>();
            param.put("leaderRangeId", userRangeId);
            List<User> users = userMapper.findAssessMiddleRangeUsers(param);
            if (MicrovanUtil.isNotEmpty(users)) {
                for (User user : users) {
                    CusAssessUser assessUser = new CusAssessUser();
                    assessUser.setUserId(user.getOid());
                    assessUser.setUserName(user.getUserName());
                    List<CusNormScore> scores = new ArrayList<>();
                    List<StandardCategory> list = standardCategoryMapper.queryList(null);
                    if (MicrovanUtil.isNotEmpty(list)) {
                        int total = 0;
                        for (StandardCategory category : list) {
                            Map<String, Object> map = new HashMap<>();
                            map.put("categoryId", Long.valueOf(category.getOid()));
                            List<StandardNorm> standardNorms = standardNormMapper.queryList(map);
                            for (StandardNorm standardNorm: standardNorms) {
                                CusNormScore normScore = new CusNormScore();
                                normScore.setNormId(standardNorm.getOid());
                                Map<String, Object> recordParam = new HashMap<>();
                                recordParam.put("yearId", assessRange.getYearId());
                                recordParam.put("userId", user.getOid());
                                recordParam.put("assessType", ProjectConstants.ASSESS_TYPE_FGLDPZC);
                                recordParam.put("assessUser", assessRange.getUserId());
                                recordParam.put("normId", standardNorm.getOid());
                                AssessRecord assessRecord = assessRecordMapper.checkAssessRecord(recordParam);
                                if (MicrovanUtil.isNotEmpty(assessRecord)) {
                                    normScore.setScore(assessRecord.getScore());
                                } else {
                                    normScore.setScore(0);
                                }
                                total = total + normScore.getScore();
                                scores.add(normScore);
                            }
                        }
                        CusNormScore cusNormScore = new CusNormScore();
                        cusNormScore.setNormId(ProjectConstants.TEMP_COUNT_ID);
                        cusNormScore.setScore(total);
                        scores.add(cusNormScore);
                    }
                    assessUser.setScores(scores);
                    assessUsers.add(assessUser);
                }
            }
        }
        model.setData(assessUsers);
        return model;
    }

    @Override
    public ResponseModel findAssessTeachers(Long yearId, Long deptId, Integer assessType, Long assessUserId, Integer userType) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        List<CusAssessTeacher> assessTeachers = new ArrayList<>();
        User findUser = userMapper.selectByPrimaryKey(assessUserId);
        if (MicrovanUtil.isNotEmpty(findUser)) {
            Map<String, Object> param = new HashMap<>();
            param.put("yearId", yearId);
            param.put("deptId", deptId);
            param.put("userType", userType);
            List<User> users = userMapper.findAssessTeachers(param);
            if (MicrovanUtil.isNotEmpty(users)) {
                for (User user : users) {
                    CusAssessTeacher assessTeacher = new CusAssessTeacher();
                    assessTeacher.setUserId(user.getOid());
                    assessTeacher.setUserName(user.getUserName());
                    assessTeacher.setDeptId(user.getDeptId());
                    assessTeacher.setDeptName(user.getDeptName());
                    assessTeacher.setUserType(ProjectUtil.convertUserType(user.getUserType()));
                    Map<String, Object> recordParam = new HashMap<>();
                    recordParam.put("yearId", yearId);
                    recordParam.put("userId", user.getOid());
                    recordParam.put("assessType", assessType);
                    recordParam.put("assessUser", assessUserId);
                    AssessRecord assessRecord = assessRecordMapper.checkAssessRecord(recordParam);
                    if (MicrovanUtil.isNotEmpty(assessRecord)) {
                        assessTeacher.setScore(assessRecord.getScore());
                    }
                    Map<String, Object> rangeParam = new HashMap<>();
                    rangeParam.put("yearId", yearId);
                    rangeParam.put("deptId", user.getDeptId());
                    rangeParam.put("userId", user.getOid());
                    rangeParam.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
                    AssessRange assessRange = assessRangeMapper.checkAssessRange(rangeParam);
                    if (MicrovanUtil.isNotEmpty(rangeParam)) {
                        assessTeacher.setRetire(assessRange.getRetire());
                        assessTeacher.setNote(assessRange.getNote());
                    }
                    assessTeachers.add(assessTeacher);
                }
            }
        }
        model.setData(assessTeachers);
        return model;
    }

    @Override
    public ResponseModel findUserData(Long oid, Long yearId) {
        ResponseModel model = new ResponseModel();
        User user = userMapper.selectByPrimaryKey(oid);
        if (MicrovanUtil.isNotEmpty(user)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("获取成功");
            CusUser cusUser = ProjectUtil.convertCusUser(user);
            if(MicrovanUtil.isNotEmpty(yearId)) {
                Map<String, Object> param = new HashMap<>();
                param.put("yearId", yearId);
                param.put("userId", oid);
                AssessRange assessRange = assessRangeMapper.checkAssessRange(param);
                if (MicrovanUtil.isNotEmpty(assessRange)) {
                    cusUser.setDeptId(assessRange.getDeptId());
                    if (MicrovanUtil.isNotEmpty(assessRange.getDeptName())) {
                        cusUser.setDeptName(assessRange.getDeptName());
                    }
                    cusUser.setUserType(assessRange.getUserType());
                    if (assessRange.getUserType() == ProjectConstants.RANGE_TYPE_PTYG) {
                        param.put("deptId", assessRange.getDeptId());
                        param.put("userType", ProjectConstants.RANGE_TYPE_BMFZR);
                        String leader = assessRangeMapper.queryRangeLeader(param);
                        if (MicrovanUtil.isNotEmpty(leader)) {
                            cusUser.setLeader(leader);
                        }
                    }
                }
            }
            model.setData(cusUser);
        }  else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("获取失败，请稍后重试");
        }
        return model;
    }

    @Override
    public Map<String, Object> importUserList(List<User> userList) {
        Map<String, Object> resultMap = new HashMap<>();
        int j = 0;
        int k = 0;
        StringBuffer msg = new StringBuffer();
        for (int i = 0; i < userList.size(); i++) {
            User user = userList.get(i);
            boolean isVia = true;
            StringBuffer itemMsg = new StringBuffer();
            itemMsg.append("第").append(user.getRowNum()).append("行：");

            if (MicrovanUtil.isEmpty(user.getUserName())) {
                itemMsg.append("姓名不能为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(user.getSex())) {
                itemMsg.append("性别不能为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(user.getMobile())) {
                itemMsg.append("手机号码不能为空；");
                isVia = false;
            } else if (!Validator.isMobile(user.getMobile())) {
                itemMsg.append("手机号码格式错误；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(user.getUserType())) {
                itemMsg.append("用户类别不能为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(user.getDegree())) {
                itemMsg.append("文化程度不能为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(user.getIdCard())) {
                itemMsg.append("身份证号不能为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(user.getLoginName())) {
                itemMsg.append("工号不能为空；");
                isVia = false;
            }
            if (MicrovanUtil.isEmpty(user.getDeptName())) {
                itemMsg.append("所在部门不能为空；");
                isVia = false;
            } else {
                Map<String, Object> param = new HashMap<>();
                param.put("deptName", user.getDeptName());
                Dept dept = deptMapper.checkDept(param);
                if (MicrovanUtil.isNotEmpty(dept)) {
                    user.setDeptId(dept.getOid());
                } else {
                    itemMsg.append("部门名称错误；");
                    isVia = false;
                }
            }
            if (isVia) {
                int line = 0;
                Map<String, Object> param = new HashMap<>();
                param.put("mobile", user.getMobile());
                User checkUser = userMapper.checkUser(param);
                if (checkUser != null) {
                    user.setOid(checkUser.getOid());
                    line = userMapper.updateByPrimaryKeySelective(user);
                } else {
                    user.setCreateTime(new Date());
                    user.setState(ProjectConstants.USER_STATE_COMMON);
                    String password = MD5Utils.GetMD5Code(ProjectConstants.DEFAULT_PASSWORD + ProjectConstants.TEAM_SIGN);
                    user.setPassword(PasswordUtil.generate(password));
                    line = userMapper.insertSelective(user);
                    if (line > 0) {
                        param.clear();
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
                    }
                }
                if (line > 0) {

                    j++;
                } else {
                    msg.append("第").append(user.getRowNum()).append("行，插入失败；");
                    k++;
                }
            } else {
                msg.append(itemMsg.toString()).append("。");
                k++;
            }
        }
        resultMap.put("okNum", j);
        resultMap.put("failNum", k);
        resultMap.put("msg", msg.toString());
        return resultMap;
    }
}
