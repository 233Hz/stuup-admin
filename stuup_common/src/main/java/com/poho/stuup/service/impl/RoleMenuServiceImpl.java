package com.poho.stuup.service.impl;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.RoleMenuMapper;
import com.poho.stuup.model.dto.RoleMenuDTO;
import com.poho.stuup.service.IRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleMenuServiceImpl implements IRoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<Long> getRoleMenu(Long roleId) {
        return roleMenuMapper.getRoleMenu(roleId);
    }

    @Override
    public ResponseModel setRoleMenu(RoleMenuDTO roleMenuDTO) {
        roleMenuMapper.removerRoleMenu(roleMenuDTO.getRoleId());
        return null;
    }

}
