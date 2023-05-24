package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.MenuMapper;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.vo.Tree;
import com.poho.stuup.service.MenuService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Tree> getMenuTree() {
        List<Menu> menus = baseMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .select(Menu::getOid, Menu::getPid, Menu::getName));
        Map<Long, Tree> map = new HashMap<>();
        for (Menu menu : menus) {
            Tree tree = new Tree();
            tree.setKey(menu.getName());
            tree.setValue(menu.getOid());
            map.put(menu.getOid(), tree);
        }
        List<Tree> menuTreeList = new ArrayList<>();
        for (Menu menu : menus) {
            Tree menuTree = map.get(menu.getOid());
            Tree parentTree = map.get(menu.getPid());
            if (parentTree != null) {
                List<Tree> children = parentTree.getChildren();
                if (CollUtil.isEmpty(children)) {
                    List<Tree> treeChildren = new ArrayList<>();
                    treeChildren.add(menuTree);
                    parentTree.setChildren(treeChildren);
                } else {
                    children.add(menuTree);
                }
            } else {
                menuTreeList.add(menuTree);
            }
        }
        return menuTreeList;
    }
}
