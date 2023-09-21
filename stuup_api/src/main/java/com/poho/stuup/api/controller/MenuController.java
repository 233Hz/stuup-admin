package com.poho.stuup.api.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckSafe;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.PermissionType;
import com.poho.stuup.constant.ValidationGroups;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.vo.MenuTree;
import com.poho.stuup.model.vo.Tree;
import com.poho.stuup.service.MenuService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping
    public ResponseModel<List<MenuTree>> getMenuTreeList() {
        return ResponseModel.ok(menuService.menuTreeList());
    }

    @GetMapping("/tree")
    public ResponseModel<List<Tree>> getMenuTree() {
        return ResponseModel.ok(menuService.getMenuTree());
    }

    @SaCheckPermission("menu_add")
    @PostMapping
    public ResponseModel<Menu> add(@Validated(ValidationGroups.ADD.class) @RequestBody Menu menu) {
        Integer type = menu.getType();
        if (PermissionType.MENU.ordinal() == type) {
            if (menu.getPath() == null) return ResponseModel.failed("菜单路径不能为空");
            if (menu.getFlag() == null) return ResponseModel.failed("菜单标识不能为空");
            if (menu.getKeepAlive() == null) return ResponseModel.failed("是否缓存不能为空");
            if (menu.getLayout() == null) return ResponseModel.failed("菜单布局不能为空");
            if (menu.getHidden() == null) return ResponseModel.failed("隐藏菜单不能为空");
        } else if (PermissionType.BUTTON.ordinal() == type) {
            if (menu.getPermission() == null) return ResponseModel.failed("权限标识不能为空");
        } else {
            return ResponseModel.failed("权限类型错误");
        }
        return ResponseModel.ok(menuService.add(menu), "添加成功");
    }

    @SaCheckPermission("menu_edit")
    @PutMapping
    public ResponseModel<Menu> edit(@Validated(ValidationGroups.Update.class) @RequestBody Menu menu) {
        Integer type = menu.getType();
        if (PermissionType.MENU.ordinal() == type) {
            if (menu.getPath() == null) return ResponseModel.failed("菜单路径不能为空");
            if (menu.getFlag() == null) return ResponseModel.failed("菜单标识不能为空");
            if (menu.getKeepAlive() == null) return ResponseModel.failed("是否缓存不能为空");
            if (menu.getLayout() == null) return ResponseModel.failed("菜单布局不能为空");
            if (menu.getHidden() == null) return ResponseModel.failed("隐藏菜单不能为空");
        } else if (PermissionType.BUTTON.ordinal() == type) {
            if (menu.getPermission() == null) return ResponseModel.failed("权限标识不能为空");
        } else {
            return ResponseModel.failed("权限类型错误");
        }
        return ResponseModel.ok(menuService.edit(menu), "修改成功");
    }

    @SaCheckSafe("menu_del")
    @SaCheckPermission("menu_del")
    @DeleteMapping
    public ResponseModel<Long> del(@RequestParam("id") Long id) {
        long count = menuService.count(Wrappers.<Menu>lambdaQuery()
                .eq(Menu::getPid, id));
        if (count > 0) return ResponseModel.failed("该项下还有子元素，无法删除");
        return ResponseModel.ok(menuService.del(id), "删除成功");
    }


}
