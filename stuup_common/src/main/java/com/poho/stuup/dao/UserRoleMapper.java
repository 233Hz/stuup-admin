package com.poho.stuup.dao;

import com.poho.stuup.model.Role;
import com.poho.stuup.model.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserRoleMapper extends BaseDao<UserRole> {
    /**
     * @param param
     * @return
     */
    List<Long> queryUserRoles(Map<String, Object> param);

    /**
     * @param userId
     */
    int clearUserRole(Long userId);

    /**
     * 清除用户所有角色
     *
     * @param userId
     * @return
     */
    int clearUserAllRole(Long userId);

    /**
     * 清除用户的某个角色
     *
     * @param param
     * @return
     */
    int clearUserRoleId(Map<String, Object> param);


    /**
     * 查询用户角色
     *
     * @param userId
     * @return
     */
    List<Long> queryUserRoleId(@Param("userId") Long userId);

    List<Role> fetchUserRoles(@Param("userId") Long userId);

    List<String> getRoleCodeListByUserId(@Param("userId") Long userId);

}