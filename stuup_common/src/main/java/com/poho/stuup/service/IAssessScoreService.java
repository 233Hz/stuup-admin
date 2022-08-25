package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.AssessScore;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 15:10 2020/10/15
 * @Modified By:
 */
public interface IAssessScoreService {
    int deleteByPrimaryKey(Long oid);

    int insert(AssessScore record);

    int insertSelective(AssessScore record);

    AssessScore selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(AssessScore record);

    int updateByPrimaryKey(AssessScore record);

    /**
     *
     * @param yearId
     * @param deptId
     * @param key
     * @return
     */
    ResponseModel findData(Long yearId, Long deptId, String key);

    /**
     * 生成最终考核分数
     * @param yearId
     * @param operUser
     * @return
     */
    ResponseModel generateData(String yearId, Long operUser);

    /**
     *
     * @param score
     * @return
     */
    ResponseModel adjustAssessScore(AssessScore score);

    /**
     *
     * @param score
     * @return
     */
    ResponseModel adjustAssessQzcp(AssessScore score);
}
