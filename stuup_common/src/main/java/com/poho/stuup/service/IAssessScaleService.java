package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.AssessScale;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:19 2020/9/2
 * @Modified By:
 */
public interface IAssessScaleService {
    int deleteByPrimaryKey(Long oid);

    int insert(AssessScale record);

    int insertSelective(AssessScale record);

    AssessScale selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(AssessScale record);

    int updateByPrimaryKey(AssessScale record);

    /**
     *
     * @param yearId
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String yearId, String key, int page, int pageSize);

    /**
     *
     * @param scale
     * @return
     */
    ResponseModel saveOrUpdate(AssessScale scale);

    /**
     *
     * @param ids
     * @return
     */
    ResponseModel del(String ids);

    /**
     *
     * @param yearId
     * @return
     */
    ResponseModel init(Long yearId);

    /**
     * 
     * @param yearId
     * @param deptId
     * @return
     */
    ResponseModel findData(Long yearId, Long deptId);
}
