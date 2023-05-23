package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.vo.Tree;

import java.util.List;

public interface MenuService extends IService<Menu> {

    /**
     * 树形菜单
     * @return
     */
    List<Tree> getMenuTree();
}