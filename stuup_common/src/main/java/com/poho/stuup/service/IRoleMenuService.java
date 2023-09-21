package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RoleMenuDTO;

import java.util.List;

public interface IRoleMenuService {
    List<Long> getRoleMenu(Long roleId);

    /**
     * 设置角色菜单
     *
     * @param roleMenuDTO
     * @return
     */
    ResponseModel setRoleMenu(RoleMenuDTO roleMenuDTO);


    /**
     * 根据用户id，获取权限code列表
     *
     * @param userId
     * @return
     */
    List<String> getMenuCodeByUserId(Long userId);

    /**
     * 根据角色code，获取权限code列表
     *
     * @param roleCode
     * @return
     */
    List<String> getPermissionByRoleCode(String roleCode);
}