package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.CacheKeyConstant;
import com.poho.stuup.dao.MenuMapper;
import com.poho.stuup.model.Menu;
import com.poho.stuup.model.vo.MenuTree;
import com.poho.stuup.model.vo.Tree;
import com.poho.stuup.service.MenuService;
import com.poho.stuup.util.TreeUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Cacheable(value = CacheKeyConstant.MENU_TREE_LIST, key = "'menuTree'")
    @Override
    public List<MenuTree> menuTreeList() {
        List<Menu> menus = baseMapper.selectList(Wrappers.lambdaQuery());
        Map<Integer, MenuTree> map = new HashMap<>();
        List<MenuTree> menuTreeList = menus.stream().map(menu -> {
            MenuTree menuTree = new MenuTree();
            menuTree.setId(menu.getId());
            menuTree.setPid(menu.getPid());
            menuTree.setName(menu.getName());
            menuTree.setCode(menu.getCode());
            menuTree.setPath(menu.getPath());
            menuTree.setIcon(menu.getIcon());
            menuTree.setType(menu.getType());
            menuTree.setFlag(menu.getFlag());
            menuTree.setSort(menu.getSort());
            menuTree.setKeepAlive(menu.getKeepAlive());
            menuTree.setPermission(menu.getPermission());
            menuTree.setLayout(menu.getLayout());
            menuTree.setHidden(menu.getHidden());
            menuTree.setRedirect(menu.getRedirect());
            menuTree.setCreateTime(menu.getCreateTime());
            map.put(menu.getId(), menuTree);
            return menuTree;
        }).collect(Collectors.toList());
        List<MenuTree> list = new ArrayList<>();
        for (MenuTree menuTree : menuTreeList) {
            MenuTree menu = map.get(menuTree.getId());
            MenuTree parentMenu = map.get(menuTree.getPid());
            if (parentMenu != null) {
                List<MenuTree> children = parentMenu.getChildren();
                if (children != null && !children.isEmpty()) {
                    children.add(menu);
                } else {
                    parentMenu.setChildren(new ArrayList<>(Collections.singletonList(menu)));
                }
            } else {
                list.add(menu);
            }
        }
        TreeUtil.sortTree(list, MenuTree::getChildren, Comparator.comparingInt(MenuTree::getSort));
        return list;
    }

    @Override
    public List<Tree> getMenuTree() {
        List<Menu> menus = baseMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .select(Menu::getId, Menu::getPid, Menu::getName, Menu::getSort));
        Map<Integer, Tree> map = new HashMap<>();
        for (Menu menu : menus) {
            Tree tree = new Tree();
            tree.setKey(menu.getName());
            tree.setValue(menu.getId());
            tree.setSort(menu.getSort());
            map.put(menu.getId(), tree);
        }
        List<Tree> menuTreeList = new ArrayList<>();
        for (Menu menu : menus) {
            Tree menuTree = map.get(menu.getId());
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
        TreeUtil.sortTree(menuTreeList, Tree::getChildren, Comparator.comparingInt(Tree::getSort));
        return menuTreeList;
    }

    @CacheEvict(value = CacheKeyConstant.MENU_TREE_LIST, key = "'menuTree'")
    @Override
    public Menu add(Menu menu) {
        baseMapper.insert(menu);
        return menu;
    }

    @CacheEvict(value = CacheKeyConstant.MENU_TREE_LIST, key = "'menuTree'")
    @Override
    public Menu edit(Menu menu) {
        baseMapper.updateById(menu);
        return menu;
    }

    @CacheEvict(value = CacheKeyConstant.MENU_TREE_LIST, key = "'menuTree'")
    @Override
    public Long del(Long id) {
        baseMapper.deleteById(id);
        return id;
    }
}
