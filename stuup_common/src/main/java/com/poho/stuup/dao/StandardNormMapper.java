package com.poho.stuup.dao;

import com.poho.stuup.model.StandardNorm;

import java.util.Map;

public interface StandardNormMapper extends BaseDao<StandardNorm> {
    /**
     *
     * @param param
     * @return
     */
    StandardNorm checkNorm(Map<String, Object> param);

    /**
     *
     * @param idArr
     * @return
     */
    int delBatchByCategory(String[] idArr);
}