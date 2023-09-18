package com.poho.stuup.saToken;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.poho.stuup.service.IRoleMenuService;
import com.poho.stuup.service.IUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义权限加载接口实现类
 */
@RequiredArgsConstructor
@Component
public class StpInterfaceImpl implements StpInterface {

    private final IUserRoleService userRoleService;

    private final IRoleMenuService roleMenuService;


    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {

        List<String> permissionList = new ArrayList<>();
        // 2. 遍历角色列表，查询拥有的权限码 如果缓存没有从数据库查询
        List<String> roleCodeList = getRoleList(loginId, loginType);
        if(CollUtil.isNotEmpty(roleCodeList)){
            for (String roleCode : roleCodeList) {
                SaSession roleSession = SaSessionCustomUtil.getSessionById(Constants.ROLE_ + roleCode);
                List<String> list = roleSession.get(SaSession.PERMISSION_LIST, () -> {
                    return roleMenuService.getMenuCodeByRoleCode(roleCode);  // 从数据库查询这个角色所拥有的权限列表
                });
                permissionList.addAll(list);
            }
        }
        // 3. 返回权限码集合
        return permissionList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SaSession session = StpUtil.getSessionByLoginId(loginId);
        return session.get(SaSession.ROLE_LIST, () -> {
            return userRoleService.getRoleCodeListByUserId(new Long(loginId.toString())); // 从数据库查询这个账号id拥有的角色列表
        });
    }

}

