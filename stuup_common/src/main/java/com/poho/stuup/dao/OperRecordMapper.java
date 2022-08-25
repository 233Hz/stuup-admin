package com.poho.stuup.dao;

import com.poho.stuup.model.OperRecord;

import java.util.Map;

public interface OperRecordMapper extends BaseDao<OperRecord> {
    /**
     *
     * @param param
     * @return
     */
    OperRecord checkOperRecord(Map<String, Object> param);

    /**
     *
     * @param param
     */
    int clearOperRecord(Map<String, Object> param);
}