package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.CusMap;
import com.poho.common.custom.PageData;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.custom.CusRangeSubmit;
import com.poho.stuup.custom.CusTransfer;
import com.poho.stuup.dao.AssessRangeMapper;
import com.poho.stuup.dao.RangeMiddleMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.dao.UserRoleMapper;
import com.poho.stuup.model.AssessRange;
import com.poho.stuup.model.User;
import com.poho.stuup.model.UserRole;
import com.poho.stuup.service.IAssessRangeService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:16 2020/9/2
 * @Modified By:
 */
@Service
public class AssessRangeServiceImpl implements IAssessRangeService {
    @Resource
    private AssessRangeMapper assessRangeMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RangeMiddleMapper rangeMiddleMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return assessRangeMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(AssessRange record) {
        return assessRangeMapper.insert(record);
    }

    @Override
    public int insertSelective(AssessRange record) {
        return assessRangeMapper.insertSelective(record);
    }

    @Override
    public AssessRange selectByPrimaryKey(Long oid) {
        return assessRangeMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(AssessRange record) {
        return assessRangeMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(AssessRange record) {
        return assessRangeMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel saveOrUpdate(AssessRange assessRange) {
        ResponseModel model = new ResponseModel();
        int line;
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", assessRange.getYearId());
        param.put("userId", assessRange.getUserId());
        param.put("userType", assessRange.getUserType());
        param.put("deptId", assessRange.getDeptId());
        if (MicrovanUtil.isNotEmpty(assessRange.getOid())) {
            param.put("oid", assessRange.getOid());
        }
        AssessRange checkAssessRange = assessRangeMapper.checkAssessRange(param);
        if (MicrovanUtil.isEmpty(checkAssessRange)) {
            long roleId = ProjectUtil.convertRangeTypeToRoleId(assessRange.getUserType());
            if (MicrovanUtil.isNotEmpty(assessRange.getOid())) {
                AssessRange tempRange = assessRangeMapper.selectByPrimaryKey(assessRange.getOid());
                if (MicrovanUtil.isNotEmpty(tempRange)) {
                    //修改时如果变更了用户
                    if (!tempRange.getUserId().equals(assessRange.getUserId())) {
                        //清除之前变更前用户的某角色
                        param.put("userId", tempRange.getUserId());
                        param.put("roleId", roleId);
                        userRoleMapper.clearUserYearRole(param);
                    }
                    //判断当前提交的用户是否有角色
                    param.put("userId", assessRange.getUserId());
                    List<Long> roleIds = userRoleMapper.queryUserYearRoles(param);
                    if (MicrovanUtil.isEmpty(roleIds) || (MicrovanUtil.isNotEmpty(roleIds) && !roleIds.contains(roleId))) {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(assessRange.getUserId());
                        userRole.setRoleId(roleId);
                        userRole.setYearId(assessRange.getYearId());
                        userRole.setCreateTime(new Date());
                        userRoleMapper.insertSelective(userRole);
                    }
                    if (MicrovanUtil.isNotEmpty(roleIds) && roleIds.contains(roleId)) {
                        param.put("roleId", roleId);
                        userRoleMapper.updateUserRoleYear(param);
                    }
                }
                assessRange.setCreateUser(null);
                line = assessRangeMapper.updateByPrimaryKeySelective(assessRange);
            } else {
                assessRange.setCreateTime(new Date());
                line = assessRangeMapper.insertSelective(assessRange);

                param.put("userId", assessRange.getUserId());
                List<Long> roleIds = userRoleMapper.queryUserYearRoles(param);
                if (MicrovanUtil.isEmpty(roleIds) || (MicrovanUtil.isNotEmpty(roleIds) && !roleIds.contains(roleId))) {
                    UserRole userRole = new UserRole();
                    userRole.setUserId(assessRange.getUserId());
                    userRole.setRoleId(roleId);
                    userRole.setYearId(assessRange.getYearId());
                    userRole.setCreateTime(new Date());
                    userRoleMapper.insertSelective(userRole);
                }
                if (MicrovanUtil.isNotEmpty(roleIds) && roleIds.contains(roleId)) {
                    param.put("roleId", roleId);
                    userRoleMapper.updateUserRoleYear(param);
                }
            }
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("保存成功");
            } else {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMessage("保存失败，请稍后重试");
            }
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("数据重复，请修改后重新提交");
        }
        return model;
    }

    @Override
    public ResponseModel findDataPageResult(String yearId, String deptId, String userType, String key, int page, int pageSize) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> map = new HashMap<>();
        map.put("key", key);
        if (MicrovanUtil.isNotEmpty(yearId)) {
            map.put("yearId", Long.valueOf(yearId));
        }
        if (MicrovanUtil.isNotEmpty(deptId)) {
            map.put("deptId", Long.valueOf(deptId));
        }
        if (MicrovanUtil.isNotEmpty(yearId)) {
            map.put("userType", Integer.valueOf(userType));
        }
        int count = assessRangeMapper.queryTotal(map);
        PageData<AssessRange> pageData = new PageData(page, pageSize, count);
        map.put("start", pageData.getStart());
        map.put("length", pageSize);
        List<AssessRange> list = assessRangeMapper.queryList(map);
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
    public ResponseModel del(String ids) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_EXCEPTION);
        model.setMessage("删除失败，请稍后重试");
        String[] idArr = ids.split(",");
        if (MicrovanUtil.isNotEmpty(idArr)) {
            for (String oid : idArr) {
                AssessRange assessRange = assessRangeMapper.selectByPrimaryKey(Long.valueOf(oid));
                if (MicrovanUtil.isNotEmpty(assessRange)) {
                    long roleId = ProjectUtil.convertRangeTypeToRoleId(assessRange.getUserType());
                    Map<String, Object> param = new HashMap<>();
                    param.put("yearId", assessRange.getYearId());
                    param.put("userId", assessRange.getUserId());
                    param.put("roleId", roleId);
                    userRoleMapper.clearUserYearRole(param);
                }
            }
            int line = assessRangeMapper.deleteBatch(idArr);
            if (line > 0) {
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("删除成功");
            }
        }
        return model;
    }

    @Override
    public ResponseModel findRangeSetUsers(Long yearId, Long deptId) {
        ResponseModel model = new ResponseModel();
        Map<String, Object> param = new HashMap<>(2);
        param.put("yearId", yearId);
        param.put("deptId", deptId);
        List<User> canUsers = userMapper.findRangeCanUsers(param);
        List<CusMap> data = new ArrayList<>();
        if (MicrovanUtil.isNotEmpty(canUsers)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
            for (User user : canUsers) {
                CusMap map = new CusMap(user.getOid(), user.getUserName());
                data.add(map);
            }
            model.setData(data);
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("无数据");
        }
        return model;
    }

    @Override
    public ResponseModel findLeaderRangeSetUsers(Long yearId, Long oid) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        Map<String, Object> param = new HashMap<>(2);
        param.put("yearId", yearId);
        param.put("oid", oid);
        List<User> canUsers = userMapper.findLeaderRangeCanUsers(param);
        List<CusMap> data = new ArrayList<>();
        if (MicrovanUtil.isNotEmpty(canUsers)) {
            for (User user : canUsers) {
                CusMap map = new CusMap(user.getOid(), user.getUserName());
                data.add(map);
            }
        }
        model.setData(data);
        return model;
    }

    @Override
    public ResponseModel findDeptRangeSetUsers(Long yearId, Long deptId) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        param.put("deptId", deptId);
        List<User> canUsersData = userMapper.findRangeCanUsers(param);
        List<CusTransfer> canUsers = ProjectUtil.convertCusTransfer(canUsersData);
        param.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
        List<Long> users = userMapper.findRangeUsers(param);
        Map<String, Object> data = new HashMap<>();
        data.put("canUsers", canUsers);
        data.put("users", users);
        model.setData(data);
        return model;
    }

    @Override
    public ResponseModel findMiddleRangeSetUsers(Long yearId, Long rangeId) {
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        Map<String, Object> param = new HashMap<>();
        param.put("yearId", yearId);
        List<AssessRange> canRangeData = assessRangeMapper.queryMiddleRange(param);
        List<CusTransfer> canRanges = ProjectUtil.convertCusRangeTransfer(canRangeData);
        param.put("leaderRangeId", rangeId);
        List<Long> ranges = rangeMiddleMapper.queryRangeMiddleIds(param);
        Map<String, Object> data = new HashMap<>();
        data.put("canRanges", canRanges);
        data.put("ranges", ranges);
        model.setData(data);
        return model;
    }

    @Override
    public ResponseModel saveMultiRange(CusRangeSubmit rangeSubmit) {
        ResponseModel model = new ResponseModel();
        if (MicrovanUtil.isNotEmpty(rangeSubmit.getUsers())) {
            Map<String, Object> param = new HashMap<>();
            param.put("yearId", rangeSubmit.getYearId());
            param.put("deptId", rangeSubmit.getDeptId());
            param.put("userType", ProjectConstants.RANGE_TYPE_PTYG);
            assessRangeMapper.delYearDeptYG(param);
            long roleId = ProjectUtil.convertRangeTypeToRoleId(ProjectConstants.RANGE_TYPE_PTYG);
            userRoleMapper.clearYearDeptUserRole(param);
            for (Long userId : rangeSubmit.getUsers()) {
                AssessRange assessRange = new AssessRange();
                assessRange.setYearId(rangeSubmit.getYearId());
                assessRange.setDeptId(rangeSubmit.getDeptId());
                assessRange.setUserType(rangeSubmit.getUserType());
                assessRange.setUserId(userId);
                assessRange.setCreateUser(rangeSubmit.getCreateUser());
                assessRange.setCreateTime(new Date());
                assessRange.setRetire(rangeSubmit.getRetire());
                assessRange.setNote(rangeSubmit.getNote());
                int line = assessRangeMapper.insertSelective(assessRange);
                if (line > 0) {
                    param.put("userId", userId);
                    List<Long> roleIds = userRoleMapper.queryUserYearRoles(param);
                    if (MicrovanUtil.isEmpty(roleIds) || (MicrovanUtil.isNotEmpty(roleIds) && !roleIds.contains(roleId))) {
                        UserRole userRole = new UserRole();
                        userRole.setUserId(assessRange.getUserId());
                        userRole.setRoleId(roleId);
                        userRole.setYearId(assessRange.getYearId());
                        userRole.setCreateTime(new Date());
                        userRoleMapper.insertSelective(userRole);
                    }
                    if (MicrovanUtil.isNotEmpty(roleIds) && roleIds.contains(roleId)) {
                        userRoleMapper.updateUserRoleYear(param);
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
}
