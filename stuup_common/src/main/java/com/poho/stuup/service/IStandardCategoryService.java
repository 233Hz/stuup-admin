package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.StandardCategory;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 14:25 2020/9/18
 * @Modified By:
 */
public interface IStandardCategoryService {
    int deleteByPrimaryKey(Long oid);

    int insert(StandardCategory record);

    int insertSelective(StandardCategory record);

    StandardCategory selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(StandardCategory record);

    int updateByPrimaryKey(StandardCategory record);

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
     * @return
     */
    ResponseModel findData();

    /**
     *
     * @param category
     * @return
     */
    ResponseModel saveOrUpdate(StandardCategory category);

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
    ResponseModel findStandardData();

    /**
     *
     * @return
     */
    ResponseModel findAssessStandardData();
}
