package com.poho.stuup.service;

import com.poho.stuup.model.UserRole;

import java.util.List;

public interface IUserRoleService {
    int deleteByPrimaryKey(Long oid);

    int insert(UserRole record);

    int insertSelective(UserRole record);

    UserRole selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(UserRole record);

    int updateByPrimaryKey(UserRole record);

    List<String> getRoleCodeListByUserId(Long userId);
}