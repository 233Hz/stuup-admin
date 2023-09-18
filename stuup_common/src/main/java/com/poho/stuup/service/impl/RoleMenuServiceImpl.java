package com.poho.stuup.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.RoleMapper;
import com.poho.stuup.dao.RoleMenuMapper;
import com.poho.stuup.model.dto.RoleMenuDTO;
import com.poho.stuup.saToken.Constants;
import com.poho.stuup.service.IRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RoleMenuServiceImpl implements IRoleMenuService {

    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private RoleMapper roleMapper;

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
            //角色权限关系变化，清空缓存中原来的关系，以便在使用中重新从数据库中获取最新关系
            SaSessionCustomUtil.deleteSessionById(Constants.ROLE_ + roleMapper.selectByPrimaryKey(roleMenuDTO.getRoleId()).getRoleCode());
            return ResponseModel.ok(null, "设置成功");
        }
        return ResponseModel.failed("设置失败");
    }



    @Override
    public List<String> getMenuCodeByUserId(Long userId) {
        return roleMenuMapper.getMenuCodeByUserId(userId);
    }
    @Override
    public  List<String> getMenuCodeByRoleCode(String roleCode) {
        return roleMenuMapper.getMenuCodeByRoleCode(roleCode);
    }

}
