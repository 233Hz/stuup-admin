package com.poho.common.util;

public class MenuUtil {

//    public static MenuTree parse(Menu menu) {
//        if (null != menu) {
//            MenuTree menuTree = new MenuTree();
//            menuTree.setId(menu.getId());
//            menuTree.setMenuName(menu.getMenuName());
//            menuTree.setMenuCode(menu.getMenuCode());
//            menuTree.setIcon(menu.getIcon());
//            menuTree.setParentId(menu.getParentId());
//            menuTree.setSourceFlag(menu.getSourceFlag());
//            menuTree.setLink(menu.getLink());
//            menuTree.setChildren(null);
//            return menuTree;
//        }
//        return null;
//    }
//
//    /**
//     * @param nodes
//     * @return
//     */
//    public static List<MenuTree> parse(List<Menu> nodes) {
//        List<MenuTree> menuTrees = null;
//        if (null != nodes && !nodes.isEmpty()) {
//            menuTrees = new ArrayList<>();
//            for (Menu module : nodes) {
//                MenuTree menuTree = parse(module);
//                if (null != menuTree) {
//                    menuTrees.add(menuTree);
//                }
//            }
//        }
//        return menuTrees;
//    }
//
//    /***
//     * 构建子节点
//     * @param node
//     * @return
//     */
//    private static List<MenuTree> buildChildren(MenuTree node, List<MenuTree> nodes) {
//        List<MenuTree> children = null;
//        Long id = node.getId();
//        for (MenuTree child : nodes) {
//            //获取子节点
//            if (id.equals(child.getParentId())) {
//                if (null == children) {
//                    children = new ArrayList<>();
//                }
//                children.add(child);
//            }
//        }
//        return children;
//    }
//
//    /**
//     * 构建下级菜单
//     *
//     * @param node
//     */
//    private static void buildNode(MenuTree node, List<MenuTree> nodes) {
//        //获取下级菜单
//        List<MenuTree> children = buildChildren(node, nodes);
//        if (null != children && !children.isEmpty()) {
//            node.setChildren(children);
//            for (MenuTree child : children) {
//                buildNode(child, nodes);
//            }
//        }
//    }
//
//    /**
//     * 构建菜单树
//     *
//     * @param nodes 权限内的菜单
//     */
//    public static void bulid(List<MenuTree> parentNodes, List<MenuTree> nodes) {
//        if (null != nodes && !nodes.isEmpty()) {
//            //保存权限内的菜单结果
//            List<MenuTree> menuTree = new ArrayList<>();
//            for (MenuTree menu : parentNodes){
//                //遍历根节点
//                buildNode(menu, nodes);
//                List<MenuTree> children = menu.getChildren();
//                //去除空的没有叶子节点
//                if (null != children && !children.isEmpty()) {
//                    menuTree.add(menu);
//                }
//            }
//            nodes.clear();
//            nodes.addAll(menuTree);
//        }
//    }
}
