package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusMiddleRangeSubmit;
import com.poho.stuup.model.RangeMiddle;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 21:47 2020/12/27
 * @Modified By:
 */
public interface IRangeMiddleService {
    int deleteByPrimaryKey(Long oid);

    int insert(RangeMiddle record);

    int insertSelective(RangeMiddle record);

    RangeMiddle selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(RangeMiddle record);

    int updateByPrimaryKey(RangeMiddle record);

    /**
     *
     * @param rangeSubmit
     * @return
     */
    ResponseModel saveMultiRange(CusMiddleRangeSubmit rangeSubmit);
}
