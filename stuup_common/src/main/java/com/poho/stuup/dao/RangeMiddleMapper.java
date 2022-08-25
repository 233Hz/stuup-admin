package com.poho.stuup.dao;

import com.poho.stuup.model.RangeMiddle;

import java.util.List;
import java.util.Map;

/**
 *
 */
public interface RangeMiddleMapper extends BaseDao<RangeMiddle> {
    /**
     *
     * @param param
     * @return
     */
    List<Long> queryRangeMiddleIds(Map<String, Object> param);

    /**
     *
     * @param param
     */
    int clearLeaderRangeMiddle(Map<String, Object> param);
}