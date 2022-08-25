package com.poho.stuup.service.impl;

import com.poho.stuup.dao.MenuMapper;
import com.poho.stuup.model.Menu;
import com.poho.stuup.service.IMenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author wupeng
 * @Description 菜单管理接口实现类
 * @Date 2020-08-07 22:14
 * @return
 */
@Service
public class MenuServiceImpl implements IMenuService {
    @Resource
    private MenuMapper menuMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return menuMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(Menu record) {
        return menuMapper.insert(record);
    }

    @Override
    public int insertSelective(Menu record) {
        return menuMapper.insertSelective(record);
    }

    @Override
    public Menu selectByPrimaryKey(Long oid) {
        return menuMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(Menu record) {
        return menuMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Menu record) {
        return menuMapper.updateByPrimaryKey(record);
    }
}
