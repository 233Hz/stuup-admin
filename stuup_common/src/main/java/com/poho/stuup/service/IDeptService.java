package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Dept;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 10:43 2019/11/30
 * @Modified By:
 */
public interface IDeptService {
    int deleteByPrimaryKey(Long oid);

    int insert(Dept record);

    int insertSelective(Dept record);

    Dept selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);

    /**
     * 分页查询部门数据
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String key, int page, int pageSize);

    /**
     *
     * @param dept
     * @return
     */
    ResponseModel saveOrUpdate(Dept dept);

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
