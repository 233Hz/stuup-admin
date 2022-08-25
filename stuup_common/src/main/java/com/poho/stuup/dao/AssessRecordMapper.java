package com.poho.stuup.dao;

import com.poho.stuup.model.AssessRecord;

import java.util.List;
import java.util.Map;

public interface AssessRecordMapper extends BaseDao<AssessRecord> {
    /**
     *
     * @param param
     * @return
     */
    AssessRecord checkAssessRecord(Map<String, Object> param);

    /**
     *
     * @param record
     * @return
     */
    int updateAdjustScore(AssessRecord record);

    /**
     * 查询已参评人数
     * @param param
     * @return
     */
    int findAssessTotal(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<AssessRecord> findAssessResult(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    int findAssessUserTotal(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    int updateAssessTeacherState(Map<String, Object> param);
}