package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Major;

import java.util.List;

public interface IMajorService {
    int deleteByPrimaryKey(Integer oid);

    int insert(Major record);

    int insertSelective(Major record);

    Major selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(Major record);

    int updateByPrimaryKey(Major record);

    /**
     * 分页查询专业信息
     *
     * @param url
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findPageResult(String key, Integer page, int pageSize);

    /**
     * 新增或修改专业
     *
     * @param major
     * @return
     */
    ResponseModel saveOrUpdate(Major major);

    /**
     *
     * @return
     */
    List<Major> findMajors();
}
