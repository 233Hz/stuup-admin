package com.poho.stuup.dao;

import com.poho.stuup.model.AssessScale;

import java.util.Map;

public interface AssessScaleMapper extends BaseDao<AssessScale> {
    /**
     *
     * @param param
     * @return
     */
    AssessScale checkAssessScale(Map<String, Object> param);
}