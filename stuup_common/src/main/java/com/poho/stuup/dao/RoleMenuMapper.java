package com.poho.stuup.dao;

import com.poho.stuup.custom.CusMenu;
import com.poho.stuup.model.RoleMenu;

import java.util.List;
import java.util.Map;

public interface RoleMenuMapper extends BaseDao<RoleMenu> {
    /**
     *
     * @param roleId
     * @return
     */
    int clearRoleMenu(Long roleId);

    /**
     *
     * @param param
     * @return
     */
    List<Long> queryRoleMenuIds(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<CusMenu> findUserMenus(Map<String, Object> param);
}