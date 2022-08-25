package com.poho.stuup.dao;

import com.poho.stuup.model.UserRole;

import java.util.List;
import java.util.Map;

public interface UserRoleMapper extends BaseDao<UserRole> {
    /**
     *
     * @param param
     * @return
     */
    List<Long> queryUserYearRoles(Map<String, Object> param);

    /**
     *
     * @param userId
     */
    int clearUserRole(Long userId);

    /**
     * 清除用户所有角色
     * @param userId
     * @return
     */
    int clearUserAllRole(Long userId);

    /**
     * 清除用户的某个角色
     * @param param
     * @return
     */
    int clearUserRoleId(Map<String, Object> param);

    /**
     * 清除年份下用户的角色
     * @param param
     */
    int clearUserYearRole(Map<String, Object> param);

    /**
     * 清除某年下指定部门下人的某角色
     * @param param
     * @return
     */
    int clearYearDeptUserRole(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    int updateUserRoleYear(Map<String, Object> param);
}