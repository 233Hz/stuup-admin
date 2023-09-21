package com.poho.stuup.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckSafe;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.Role;
import com.poho.stuup.model.dto.RoleMenuDTO;
import com.poho.stuup.service.IRoleMenuService;
import com.poho.stuup.service.IRoleService;
import com.poho.stuup.util.ProjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 13:52 2020/9/3
 * @Modified By:
 */
@Api(tags = "角色管理相关接口")
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private IRoleService roleService;
    @Resource
    private IRoleMenuService roleMenuService;
    @Resource
    private HttpServletRequest request;

    /**
     * 获取所有角色
     *
     * @return
     */
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true)})
    @ApiOperation(value = "获取所有角色", httpMethod = "GET")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size) {
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        return roleService.findDataPageResult(key, page, pageSize);
    }

    /**
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseModel all() {
        return roleService.findData();
    }

    /**
     * @param role
     * @return
     */
    @SaCheckPermission("role_add_edit")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel save(@RequestBody Role role) {
        String sesUser = ProjectUtil.obtainLoginUserId(request);
        role.setCreateUser(Long.valueOf(sesUser));
        return roleService.saveOrUpdate(role);
    }

    /**
     * @param params
     * @return
     */
    @SaCheckSafe("role_del")
    @SaCheckPermission("role_del")
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseModel del(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return roleService.del(ids);
    }

    /**
     * 查询角色菜单
     *
     * @param roleId
     * @return
     */
    @GetMapping("/getRoleMenu/{roleId}")
    public ResponseModel<List<Long>> getRoleMenu(@PathVariable("roleId") Long roleId) {
        return ResponseModel.ok(roleMenuService.getRoleMenu(roleId));
    }

    @SaCheckPermission("role_auth")
    @PostMapping("/setRoleMenu")
    public ResponseModel setRoleMenu(@RequestBody @Valid RoleMenuDTO roleMenuDTO) {
        return roleMenuService.setRoleMenu(roleMenuDTO);
    }
}
