package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.*;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusUser;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.util.ProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private DeptMapper deptMapper;

    @Resource
    private YearMapper yearMapper;

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
    public ResponseModel checkLogin(String loginName, String password) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("账号或密码错误");
        if (StrUtil.isBlank(loginName)) {
            return model;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", loginName);
        User user = userMapper.checkUser(map);
        logger.info(String.format("登录 loginName:%s , pwd:%s", loginName, password));
        if (MicrovanUtil.isNotEmpty(user)) {
            if (PasswordUtil.verify(password, user.getPassword())) { //验证密码
                if (user.getState().intValue() == ProjectConstants.USER_STATE_COMMON) {
                    map.put("userId", user.getOid());
                    List<Long> roleIds = userRoleMapper.queryUserRoles(map);
                    if (MicrovanUtil.isNotEmpty(roleIds)) {
                        Year currYear = yearMapper.findCurrYear();
                        CusUser cusUser = new CusUser();
                        cusUser.setUserId(user.getOid());
                        cusUser.setLoginName(user.getLoginName());
                        cusUser.setUserName(user.getUserName());
                        cusUser.setMobile(user.getMobile());
                        cusUser.setDeptId(user.getDeptId());
                        cusUser.setUserType(user.getUserType());
                        cusUser.setRoleIds(ProjectUtil.splitListUseComma(roleIds));
                        cusUser.setYearId(currYear.getOid());
                        model.setCode(CommonConstants.CODE_SUCCESS);
                        model.setMessage("登录成功");
                        model.setToken(JwtUtil.createOneDayJwt(user.getOid().toString()));
                        model.setData(cusUser);
                    } else {
                        model.setCode(CommonConstants.CODE_EXCEPTION);
                        model.setMessage("用户未设置角色，暂无法登录");
                    }
                } else {
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                    model.setMessage("你的账号已被禁止登录系统");
                }
            }
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
            map.clear();
            for (User user : list) {
                map.put("userId", user.getOid());
                List<Long> roleIds = userRoleMapper.queryUserRoles(map);
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
    public ResponseModel findUserData(Long oid, Long yearId) {
        ResponseModel model = new ResponseModel();
        User user = userMapper.selectByPrimaryKey(oid);
        if (MicrovanUtil.isNotEmpty(user)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("获取成功");
            CusUser cusUser = ProjectUtil.convertCusUser(user);
            model.setData(cusUser);
        } else {
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
                        List<Long> roleIds = userRoleMapper.queryUserRoles(param);
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

    @Override
    public ResponseModel<List<Menu>> queryUserAuthority(long userId) {
        List<Long> roleIds = userRoleMapper.queryUserRoleId(userId);
        List<Menu> menus = roleMenuMapper.queryUserMenus(roleIds);
        return ResponseModel.ok(menus);
    }
}
