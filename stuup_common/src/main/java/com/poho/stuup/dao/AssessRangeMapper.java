package com.poho.stuup.dao;

import com.poho.stuup.model.AssessRange;

import java.util.List;
import java.util.Map;

public interface AssessRangeMapper extends BaseDao<AssessRange> {
    /**
     *
     * @param param
     * @return
     */
    AssessRange checkAssessRange(Map<String, Object> param);

    /**
     *
     * @param assessRange
     * @return
     */
    int updateData(AssessRange assessRange);

    /**
     *
     * @param param
     * @return
     */
    int delYearDeptYG(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<AssessRange> queryMiddleRange(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    String queryRangeLeader(Map<String, Object> param);
}