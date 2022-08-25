package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Role;

public interface IRoleService {
    int deleteByPrimaryKey(Long oid);

    int insert(Role record);

    int insertSelective(Role record);

    Role selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(Role record);

    int updateByPrimaryKey(Role record);

    /**
     *
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String key, int page, int pageSize);

    /**
     *
     * @param role
     * @return
     */
    ResponseModel saveOrUpdate(Role role);

    /**
     *
     * @param ids
     * @return
     */
    ResponseModel del(String ids);

    /**
     *
     * @return
     */
    ResponseModel findData();
}