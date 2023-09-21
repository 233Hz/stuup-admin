package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.vo.MenuTree;
import com.poho.stuup.model.vo.Tree;

import java.util.List;

public interface MenuService extends IService<Menu> {

    /**
     * 树形菜单
     *
     * @return
     */
    List<Tree> getMenuTree();

    List<MenuTree> menuTreeList();

    /**
     * 添加
     *
     * @param menu
     * @return
     */
    Menu add(Menu menu);

    /**
     * 修改
     *
     * @param menu
     * @return
     */
    Menu edit(Menu menu);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Long del(Long id);
}