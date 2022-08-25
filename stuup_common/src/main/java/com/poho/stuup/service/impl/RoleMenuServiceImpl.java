package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.custom.CusMenu;
import com.poho.stuup.custom.TreeData;
import com.poho.stuup.dao.MenuMapper;
import com.poho.stuup.dao.RoleMenuMapper;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.RoleMenu;
import com.poho.stuup.service.IRoleMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author wupeng
 * @Description 角色菜单管理接口实现类
 * @Date 2020-08-07 22:17
 * @return
 */
@Service
public class RoleMenuServiceImpl implements IRoleMenuService {
    @Resource
    private RoleMenuMapper roleMenuMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return roleMenuMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(RoleMenu record) {
        return roleMenuMapper.insert(record);
    }

    @Override
    public int insertSelective(RoleMenu record) {
        return roleMenuMapper.insertSelective(record);
    }

    @Override
    public RoleMenu selectByPrimaryKey(Long oid) {
        return roleMenuMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(RoleMenu record) {
        return roleMenuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(RoleMenu record) {
        return roleMenuMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel findRoleMenuData(Long roleId) {
        List<TreeData> roleMenuTrees = new ArrayList<>();
        Map<String, Object> param = new HashMap<>();
        param.put("parentId", 100);
        List<Menu> menus = menuMapper.queryList(param);
        for (Menu menu : menus) {
            TreeData treeData = new TreeData();
            treeData.setId(menu.getOid());
            treeData.setLabel(menu.getMenuName());
            param.put("parentId", menu.getOid());
            List<Menu> childMenus = menuMapper.queryList(param);
            if (MicrovanUtil.isNotEmpty(childMenus)) {
                List<TreeData> children = new ArrayList<>();
                for (Menu child: childMenus) {
                    TreeData childTree = new TreeData();
                    childTree.setId(child.getOid());
                    childTree.setLabel(child.getMenuName());
                    children.add(childTree);
                }
                treeData.setChildren(children);
            }
            roleMenuTrees.add(treeData);
        }
        param.put("roleId", roleId);
        List<Long> roleMenuIds = roleMenuMapper.queryRoleMenuIds(param);
        Map<String, Object> roleMenus = new HashMap<>();
        roleMenus.put("treeData", roleMenuTrees);
        roleMenus.put("checkedIds", roleMenuIds);
        ResponseModel model = new ResponseModel();
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setMessage("请求成功");
        model.setData(roleMenus);
        return model;
    }

    @Override
    public ResponseModel updateRoleMenu(Long roleId, String ids) {
        ResponseModel model = new ResponseModel();
        List<String> idList = new ArrayList<>();
        if (MicrovanUtil.isNotEmpty(ids)) {
            idList = Arrays.asList(ids.split(","));
        }
        if (MicrovanUtil.isNotEmpty(idList)) {
            roleMenuMapper.clearRoleMenu(roleId);
            RoleMenu baseMenu = new RoleMenu();
            baseMenu.setRoleId(roleId);
            baseMenu.setMenuId(100L);
            roleMenuMapper.insertSelective(baseMenu);
            for (String oid : idList) {
                RoleMenu roleMenu = new RoleMenu();
                roleMenu.setRoleId(roleId);
                roleMenu.setMenuId(Long.valueOf(oid));
                roleMenuMapper.insertSelective(roleMenu);
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("权限分配成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("权限分配失败，请稍后重试");
        }
        return model;
    }

    @Override
    public List<CusMenu> findUserMenus(Long userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        List<Menu> menus = menuMapper.queryUserMenus(param);
        List<CusMenu> cusMenus = new ArrayList<>();
        if (MicrovanUtil.isNotEmpty(menus)) {
            for (Menu menu : menus) {
                CusMenu cusMenu = new CusMenu();
                cusMenu.setMenuId(menu.getOid());
                cusMenu.setMenuName(menu.getMenuName());
                cusMenu.setMenuCode(menu.getMenuCode());
                cusMenu.setLink(menu.getLink());
                cusMenu.setIcon(menu.getIcon());
                cusMenu.setParentMenu(menu.getParentId());
                cusMenus.add(cusMenu);
            }
        }
        return cusMenus;
    }
}
