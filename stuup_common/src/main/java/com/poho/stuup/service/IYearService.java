package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Year;

public interface IYearService {
    int deleteByPrimaryKey(Long oid);

    int insert(Year record);

    int insertSelective(Year record);

    Year selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(Year record);

    int updateByPrimaryKey(Year record);

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
     * @param year
     * @return
     */
    ResponseModel saveOrUpdate(Year year);

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
    ResponseModel queryList();

    /**
     *
     * @param oid
     * @return
     */
    ResponseModel updateCurrYear(Long oid);

    /**
     *
     * @param yearId
     * @return
     */
    ResponseModel findData(Long yearId);
}