package com.poho.stuup.service;

import com.poho.stuup.model.Menu;

/**
 * @Author wupeng
 * @Description 菜单管理接口
 * @Date 2020-08-07 22:03
 * @return
 */
public interface IMenuService {
    int deleteByPrimaryKey(Long oid);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);
}