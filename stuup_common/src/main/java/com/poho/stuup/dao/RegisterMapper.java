package com.poho.stuup.dao;

import com.poho.stuup.model.Register;

import java.util.Map;

public interface RegisterMapper extends BaseDao<Register> {
    /**
     *
     * @param param
     * @return
     */
    Register checkRegister(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    int batchUpdate(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    int findRegTotal(Map<String, Object> param);
}