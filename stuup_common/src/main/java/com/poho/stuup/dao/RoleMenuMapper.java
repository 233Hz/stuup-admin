package com.poho.stuup.dao;

import com.poho.stuup.custom.CusMenu;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.RoleMenu;
import org.apache.ibatis.annotations.Param;

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

    /**
     * @description: 查询角色权限
     * @param: roleId   角色id
     * @return: java.util.List<com.poho.stuup.model.Menu>
     * @author BUNGA
     * @date: 2023/5/9 16:01
     */
    List<Menu> queryUserMenus(@Param("roleIds") List<Long> roleIds);
}