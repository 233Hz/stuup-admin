package com.poho.stuup.dao;

import com.poho.stuup.model.AssessState;

import java.util.Map;

/**
 *
 */
public interface AssessStateMapper extends BaseDao<AssessState> {
    /**
     *
     * @param param
     * @return
     */
    AssessState checkAssessState(Map<String, Object> param);
}