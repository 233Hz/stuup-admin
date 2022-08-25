package com.poho.stuup.dao;

import com.poho.stuup.model.AssessScore;

public interface AssessScoreMapper extends BaseDao<AssessScore> {
    /**
     *
     * @param yearId
     * @return
     */
    int delYearScore(Long yearId);

    /**
     *
     * @param score
     * @return
     */
    int updateAdjustScore(AssessScore score);

    /**
     *
     * @param score
     * @return
     */
    int updateAdjustQzcp(AssessScore score);

    /**
     *
     * @param oid
     * @return
     */
    int reCalculateScore(Long oid);
}