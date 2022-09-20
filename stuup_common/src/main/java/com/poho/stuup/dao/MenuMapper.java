package com.poho.stuup.dao;

import com.poho.stuup.model.Menu;

import java.util.List;
import java.util.Map;

public interface MenuMapper extends BaseDao<Menu> {
    /**
     *
     * @param param
     * @return
     */
    List<Menu> queryUserMenus(Map<String, Object> param);

    List<Menu> queryParentMenus();
}