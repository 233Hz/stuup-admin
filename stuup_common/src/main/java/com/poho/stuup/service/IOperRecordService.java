package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.OperRecord;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 15:00 2020/9/18
 * @Modified By:
 */
public interface IOperRecordService {
    int deleteByPrimaryKey(Long oid);

    int insert(OperRecord record);

    int insertSelective(OperRecord record);

    OperRecord selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(OperRecord record);

    int updateByPrimaryKey(OperRecord record);

    /**
     *
     * @param yearId
     * @param operType
     * @return
     */
    ResponseModel findOperData(Long yearId, Integer operType);

    /**
     *
     * @param record
     * @return
     */
    ResponseModel sendNorm(OperRecord record);

    /**
     * 查询考核状态
     * @param yearId
     * @return
     */
    ResponseModel findAssessState(Long yearId);
}
