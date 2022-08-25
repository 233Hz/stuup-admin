package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.StandardNorm;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 14:26 2020/9/18
 * @Modified By:
 */
public interface IStandardNormService {
    int deleteByPrimaryKey(Long oid);

    int insert(StandardNorm record);

    int insertSelective(StandardNorm record);

    StandardNorm selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(StandardNorm record);

    int updateByPrimaryKey(StandardNorm record);

    /**
     *
     * @param categoryId
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String categoryId, String key, int page, int pageSize);

    /**
     *
     * @param standardNorm
     * @return
     */
    ResponseModel saveOrUpdate(StandardNorm standardNorm);

    /**
     *
     * @param ids
     * @return
     */
    ResponseModel del(String ids);
}
