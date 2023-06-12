package com.poho.stuup.dao;

import com.poho.stuup.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseDao<Role> {
    /**
     * @param param
     * @return
     */
    Role checkRole(Map<String, Object> param);

    /**
     * 根据角色id查询角色名称
     *
     * @param roleIds
     * @return
     */
    List<String> queryRoleNames(List<Long> roleIds);

    /**
     * @description: 通过角色名称查找角色id
     * @param: superAdminName
     * @return: java.lang.Long
     * @author BUNGA
     * @date: 2023/5/30 18:55
     */
    Long findRoleIdByName(@Param("roleName") String roleName);

}