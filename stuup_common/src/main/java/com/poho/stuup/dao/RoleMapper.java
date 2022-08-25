package com.poho.stuup.dao;

import com.poho.stuup.model.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseDao<Role> {
    /**
     *
     * @param param
     * @return
     */
    Role checkRole(Map<String, Object> param);

    /**
     * 根据角色id查询角色名称
     * @param roleIds
     * @return
     */
    List<String> queryRoleNames(List<Long> roleIds);
}