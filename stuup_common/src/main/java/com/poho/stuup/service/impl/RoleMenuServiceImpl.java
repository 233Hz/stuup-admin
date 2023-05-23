package com.poho.stuup.service.impl;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.RoleMenuMapper;
import com.poho.stuup.model.dto.RoleMenuDTO;
import com.poho.stuup.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel setRoleMenu(RoleMenuDTO roleMenuDTO) {
        roleMenuMapper.removerRoleMenu(roleMenuDTO.getRoleId());
        int line = roleMenuMapper.setRoleMenu(roleMenuDTO.getRoleId(), roleMenuDTO.getMenuIds());
        if (line > 0) {
            return ResponseModel.ok(null, "设置成功");
        }
        return ResponseModel.failed("设置失败");
    }

}
