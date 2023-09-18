package com.poho.stuup.service.impl;

import com.poho.stuup.dao.UserRoleMapper;
import com.poho.stuup.model.UserRole;
import com.poho.stuup.service.IUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author wupeng
 * @Description 用户角色管理接口实现类
 * @Date 2020-08-07 22:24
 * @return
 */
@Service
public class UserRoleServiceImpl implements IUserRoleService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public int deleteByPrimaryKey(Long oid) {
        return userRoleMapper.deleteByPrimaryKey(oid);
    }

    @Override
    public int insert(UserRole record) {
        return userRoleMapper.insert(record);
    }

    @Override
    public int insertSelective(UserRole record) {
        return userRoleMapper.insertSelective(record);
    }

    @Override
    public UserRole selectByPrimaryKey(Long oid) {
        return userRoleMapper.selectByPrimaryKey(oid);
    }

    @Override
    public int updateByPrimaryKeySelective(UserRole record) {
        return userRoleMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserRole record) {
        return userRoleMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<String> getRoleCodeListByUserId(Long userId){
        return userRoleMapper.getRoleCodeListByUserId(userId);
    }
}
