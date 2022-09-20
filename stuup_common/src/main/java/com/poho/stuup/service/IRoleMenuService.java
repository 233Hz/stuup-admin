package com.poho.stuup.service;

import com.poho.common.custom.MenuTree;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusMenu;
import com.poho.stuup.model.RoleMenu;

import java.util.List;

public interface IRoleMenuService {
    int deleteByPrimaryKey(Long oid);

    int insert(RoleMenu record);

    int insertSelective(RoleMenu record);

    RoleMenu selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(RoleMenu record);

    int updateByPrimaryKey(RoleMenu record);

    /**
     *
     * @param roleId
     * @return
     */
    ResponseModel findRoleMenuData(Long roleId);

    /**
     *
     * @param roleId
     * @param ids
     * @return
     */
    ResponseModel updateRoleMenu(Long roleId, String ids);

    /**
     *
     * @param userId
     * @return
     */
    List<CusMenu> findUserMenus(Long userId);

    List<MenuTree> findUserMenuTree(Long userId);

}