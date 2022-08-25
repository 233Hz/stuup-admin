package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.AssessState;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 20:40 2020/12/13
 * @Modified By:
 */
public interface IAssessStateService {
    int deleteByPrimaryKey(Long oid);

    int insert(AssessState record);

    int insertSelective(AssessState record);

    AssessState selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(AssessState record);

    int updateByPrimaryKey(AssessState record);

    /**
     *
     * @param yearId
     * @param userId
     * @param assessType
     * @return
     */
    ResponseModel findAssessState(Long yearId, Long userId, String assessType);
}
