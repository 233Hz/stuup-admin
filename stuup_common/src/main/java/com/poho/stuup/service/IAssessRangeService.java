package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusRangeSubmit;
import com.poho.stuup.model.AssessRange;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:15 2020/9/2
 * @Modified By:
 */
public interface IAssessRangeService {
    int deleteByPrimaryKey(Long oid);

    int insert(AssessRange record);

    int insertSelective(AssessRange record);

    AssessRange selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(AssessRange record);

    int updateByPrimaryKey(AssessRange record);

    /**
     *
     * @param assessRange
     * @return
     */
    ResponseModel saveOrUpdate(AssessRange assessRange);

    /**
     *
     * @param yearId
     * @param deptId
     * @param userType
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String yearId, String deptId, String userType, String key, int page, int pageSize);

    /**
     *
     * @param ids
     * @return
     */
    ResponseModel del(String ids);

    /**
     *
     * @param yearId
     * @param deptId
     * @return
     */
    ResponseModel findRangeSetUsers(Long yearId, Long deptId);

    /**
     *
     * @param yearId
     * @param oid
     * @return
     */
    ResponseModel findLeaderRangeSetUsers(Long yearId, Long oid);

    /**
     *
     * @param yearId
     * @param deptId
     * @return
     */
    ResponseModel findDeptRangeSetUsers(Long yearId, Long deptId);

    /**
     *
     * @param rangeSubmit
     * @return
     */
    ResponseModel saveMultiRange(CusRangeSubmit rangeSubmit);

    /**
     *
     * @param yearId
     * @param rangeId
     * @return
     */
    ResponseModel findMiddleRangeSetUsers(Long yearId, Long rangeId);
}
