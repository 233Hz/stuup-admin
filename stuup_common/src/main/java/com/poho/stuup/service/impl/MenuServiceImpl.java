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

@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<Tree> getMenuTree() {
        List<Menu> menus = baseMapper.selectList(Wrappers.<Menu>lambdaQuery()
                .select(Menu::getOid, Menu::getPid, Menu::getName));
        HashMap<Long, Tree> map = new HashMap<>();
        for (Menu menu : menus) {
            Tree tree = new Tree();
            tree.setKey(menu.getName());
            tree.setValue(menu.getOid());
            map.put(menu.getOid(), tree);
        }
        List<Tree> menuTree = new ArrayList<>();
        for (Menu menu : menus) {
            Tree tree = map.get(menu.getOid());
            Tree parentTree = map.get(menu.getPid());
            if (parentTree != null) {
                List<Tree> children = parentTree.getChildren();
                if (CollUtil.isEmpty(children)) {
                    ArrayList<Tree> trees = new ArrayList<>();
                    trees.add(tree);
                    parentTree.setChildren(trees);
                }else {
                    children.add(tree);
                }
            }else {
                menuTree.add(tree);
            }
        }
        return menuTree;
    }
}
