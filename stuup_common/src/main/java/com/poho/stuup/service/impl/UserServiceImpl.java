package com.poho.stuup.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.common.util.PasswordUtil;
import com.poho.common.util.Validator;
import com.poho.stuup.constant.PermissionType;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.custom.CusUser;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.GrowthItemUserDTO;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.model.vo.GrowthItemUserVO;
import com.poho.stuup.model.vo.UserInfoPermissionVO;
import com.poho.stuup.service.*;
import com.poho.stuup.util.MinioUtils;
import com.poho.stuup.util.ProjectUtil;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
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

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private LoginLogMapper loginLogMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private GrowthItemMapper growthItemMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private StuScoreLogMapper stuScoreLogMapper;

    @Resource
    private StuScoreService stuScoreService;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private SemesterService semesterService;

    @Resource
    private StpInterface stpInterface;

    @Resource
    private FlowerModelService flowerModelService;

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
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<CusUser> checkLogin(String loginName, String password) {
        ResponseModel<CusUser> model = new ResponseModel<>();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMsg("账号或密码错误");
        if (StrUtil.isBlank(loginName)) {
            return model;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", loginName);
        User user = userMapper.checkUser(map);
        logger.info(String.format("登录 loginName:%s , pwd:%s", loginName, password));
        if (MicrovanUtil.isNotEmpty(user)) {
            if (PasswordUtil.verify(password, user.getPassword())) { //验证密码
                if (user.getState() == ProjectConstants.USER_STATE_COMMON) {
                    map.put("userId", user.getOid());
                    List<Long> roleIds = userRoleMapper.queryUserRoles(map);
                    if (MicrovanUtil.isNotEmpty(roleIds)) {
                        String message = userLoginHandler(user.getOid(), user.getUserType(), user.getLoginName());
                        model.setCode(CommonConstants.CODE_SUCCESS);
                        model.setMsg(message != null ? message : "登录成功");
                        //sa-token
                        StpUtil.login(user.getOid());
                        model.setTokenInfo(StpUtil.getTokenInfo());
                    } else {
                        model.setCode(CommonConstants.CODE_EXCEPTION);
                        model.setMsg("用户未设置角色，暂无法登录");
                    }
                } else {
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                    model.setMsg("你的账号已被禁止登录系统");
                }
            }
        }
        return model;
    }

    private String userLoginHandler(Long userId, Integer userType, String loginName) {
        String message = null;
        // 每日登入
        if (userType == UserTypeEnum.STUDENT.getValue()) {
            Student student = studentMapper.getStudentForStudentNO(loginName);
            if (student != null) {
                Integer studentId = student.getId();
                // 查询当天的登入次数
                int count = loginLogMapper.findTodayLoginCount(userId);
                if (count == 0) {
                    Long yearId = yearMapper.getCurrentYearId();
                    Long semesterId = semesterMapper.getCurrentSemesterId();
                    if (yearId != null && semesterId != null) {
                        GrowthItem growthItem = growthItemMapper.selectOne(Wrappers.<GrowthItem>lambdaQuery()
                                .eq(GrowthItem::getCode, "CZ_066"));
                        if (growthItem != null) {
                            RecAddScore recAddScore = new RecAddScore();
                            recAddScore.setYearId(yearId);
                            recAddScore.setSemesterId(semesterId);
                            recAddScore.setStudentId(Long.valueOf(studentId));
                            recAddScore.setGrowId(growthItem.getId());
                            recAddScore.setScore(growthItem.getScore());
                            recAddScoreService.save(recAddScore);
                            StuScoreLog stuScoreLog = new StuScoreLog();
                            stuScoreLog.setYearId(yearId);
                            stuScoreLog.setSemesterId(semesterId);
                            stuScoreLog.setStudentId(Long.valueOf(studentId));
                            stuScoreLog.setGrowId(growthItem.getId());
                            stuScoreLog.setScore(growthItem.getScore());
                            stuScoreLogMapper.insert(stuScoreLog);
                            stuScoreService.updateTotalScore(Long.valueOf(studentId), growthItem.getScore());

                            message = StrUtil.format("登入成功（+{}分）", growthItem.getScore());
                        }
                    }
                }
            }
        }
        // 保存登入日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(userId);
        loginLogMapper.insert(loginLog);

        return message;
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
            model.setMsg("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("无数据");
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
            model.setMsg("手机号码已存在");
            return model;
        } else {
            if (MicrovanUtil.isNotEmpty(user.getOid())) {
                line = userMapper.updateByPrimaryKeySelective(user);
            } else {
                user.setCreateTime(new Date());
                //String password = MD5Utils.GetMD5Code(ProjectConstants.DEFAULT_PASSWORD + ProjectConstants.TEAM_SIGN);
                user.setPassword(PasswordUtil.generate(ProjectConstants.DEFAULT_PASSWORD));
                line = userMapper.insertSelective(user);
            }
        }
        if (line > 0) {
            if (MicrovanUtil.isNotEmpty(user.getRoles())) {
                //修改用户信息时，清除用户角色只会清除不带year_id角色
                userRoleMapper.clearUserRole(user.getOid());
                for (Long roleId : user.getRoles()) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(user.getOid());
                    userRole.setRoleId(roleId);
                    userRole.setCreateTime(new Date());
                    userRoleMapper.insertSelective(userRole);
                }
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("保存失败，请稍后重试");
        }
        return model;
    }

    @Override
    public ResponseModel del(String ids) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMsg("删除失败，请稍后重试");
        String[] idArr = ids.split(",");
        if (MicrovanUtil.isNotEmpty(idArr)) {
            int line = userMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMsg("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel queryList() {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMsg("请求成功");
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
        model.setMsg("修改失败，请稍后重试");
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
                            model.setMsg("修改成功，请重新登录");
                        }
                    } else {
                        model.setCode(CommonConstants.CODE_EXCEPTION);
                        model.setMsg("两次新密码输入不一致");
                    }
                } else {
                    model.setCode(CommonConstants.CODE_EXCEPTION);
                    model.setMsg("旧密码错误，请重新输入");
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
            model.setMsg("获取成功");
            CusUser cusUser = ProjectUtil.convertCusUser(user);
            model.setData(cusUser);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("获取失败，请稍后重试");
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
                    //String password = MD5Utils.GetMD5Code(ProjectConstants.DEFAULT_PASSWORD + ProjectConstants.TEAM_SIGN);
                    user.setPassword(PasswordUtil.generate(ProjectConstants.DEFAULT_PASSWORD));
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
                msg.append(itemMsg).append("。");
                k++;
            }
        }
        resultMap.put("okNum", j);
        resultMap.put("failNum", k);
        resultMap.put("msg", msg.toString());
        return resultMap;
    }

    public CusUser getUserInfo(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        return ProjectUtil.convertCusUser(user);
    }

    @Override
    public ResponseModel<List<Menu>> queryUserAuthority(long userId) {
        List<Long> roleIds = userRoleMapper.queryUserRoleId(userId);
        List<Menu> menus = roleMenuMapper.queryUserMenus(roleIds);
        return ResponseModel.ok(menus);
    }

    @Override
    public IPage<GrowthItemUserVO> paginateGrowthItemUser(Page<GrowthItemUserVO> page, GrowthItemUserDTO query) {
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("userType", query.getUserType());
        queryMap.put("state", 1);
        int total = userMapper.queryTotal(queryMap);
        long start = page.getCurrent() == 1 ? 1 : page.getCurrent() * page.getSize();
        List<GrowthItemUserVO> users = userMapper.paginateGrowthItemUser(start, page.getSize(), query);
        page.setTotal(total);
        page.setRecords(users);
        return page;
    }

    @SneakyThrows
    @Override
    public ResponseModel<String> updateUserAvatar(Long userId, Long avatarId) {
        int line = userMapper.updateUserAvatar(userId, avatarId);
        if (line != 1) return ResponseModel.failed("更新头像失败");
        File file = fileMapper.selectById(avatarId);
        String bucket = file.getBucket();
        String storageName = file.getStorageName();
        String url = MinioUtils.getPreSignedObjectUrl(bucket, storageName);
        return ResponseModel.ok(url, "更新成功！");
    }

    @Override
    public UserInfoPermissionVO getUserInfoPermission(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        UserInfoPermissionVO.UserInfo userInfo = UserInfoPermissionVO.UserInfo.builder().userId(user.getOid()).userName(user.getUserName()).userType(user.getUserType()).build();
        Optional.ofNullable(user.getAvatarId())
                .flatMap(avatarId -> Optional.ofNullable(fileMapper.selectById(avatarId)))
                .flatMap(file -> {
                    try {
                        return Optional.ofNullable(MinioUtils.getPreSignedObjectUrl(file.getBucket(), file.getStorageName()));
                    } catch (Exception e) {
                        logger.error("{}用户头像获取失败", user.getUserName());
                        return Optional.empty();
                    }
                })
                .ifPresent(userInfo::setAvatarUrl);
        List<Role> roles = userRoleMapper.fetchUserRoles(userId);
        List<String> roleCodes = new ArrayList<>();
        List<Menu> menus = new ArrayList<>();
        List<String> permissions = new ArrayList<>();
        if (roles != null && !roles.isEmpty()) {
            List<Long> roleIds = new ArrayList<>();
            for (Role role : roles) {
                roleIds.add(role.getOid());
                roleCodes.add(role.getRoleCode());
            }
            List<Menu> list = roleMenuMapper.queryUserMenus(roleIds);
            if (list != null && !list.isEmpty()) {
                for (Menu menu : list) {
                    if (menu.getType() == PermissionType.BUTTON.ordinal()) {
                        permissions.add(menu.getPermission());
                    } else if (menu.getType() == PermissionType.MENU.ordinal()) {
                        menus.add(menu);
                    }
                }
            }
        }
        UserInfoPermissionVO.OtherInfo otherInfo = UserInfoPermissionVO.OtherInfo.builder().build();
        Optional.ofNullable(yearMapper.getCurrentYearId()).ifPresent(otherInfo::setYearId);
        Optional.ofNullable(semesterMapper.getCurrentSemesterId()).ifPresent(otherInfo::setSemesterId);
        UserInfoPermissionVO.GrowthInfo growthInfo = UserInfoPermissionVO.GrowthInfo.builder().totalScore(BigDecimal.ZERO).build();
        Optional.ofNullable(user.getLoginName())
                .flatMap(loginName -> Optional.ofNullable(studentMapper.getStudentForStudentNO(loginName)))
                .ifPresent(student -> {
                    otherInfo.setStudentId(student.getId());
                    StuScore stuScore = stuScoreService.getOne(Wrappers.<StuScore>lambdaQuery()
                            .select(StuScore::getScore)
                            .eq(StuScore::getStudentId, student.getId()));
                    Optional.ofNullable(recAddScoreService.getStudentNowRanking(Long.valueOf(student.getId()))).ifPresent(growthInfo::setRank);
                    if (stuScore != null) {
                        // 获取未收取的分数
                        List<RecAddScore> unCollectScores = recAddScoreService.list(Wrappers.<RecAddScore>lambdaQuery()
                                .select(RecAddScore::getScore)
                                .eq(RecAddScore::getStudentId, student.getId())
                                .eq(RecAddScore::getState, WhetherEnum.NO.getValue()));
                        BigDecimal unCollectScore = unCollectScores.stream().map(RecAddScore::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
                        growthInfo.setTotalScore(stuScore.getScore().subtract(unCollectScore));
                    }
                });
        FlowerVO flowerModel = flowerModelService.getFlowerModel();
        return UserInfoPermissionVO.builder().userInfo(userInfo).menus(menus).roles(roleCodes).permissions(permissions).flowerConfig(flowerModel).growthInfo(growthInfo).otherInfo(otherInfo).build();
    }
}
