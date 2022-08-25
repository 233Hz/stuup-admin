package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusAssessSubmit;
import com.poho.stuup.custom.CusTeacherSubmit;
import com.poho.stuup.model.AssessRecord;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 19:17 2020/9/2
 * @Modified By:
 */
public interface IAssessRecordService {
    int deleteByPrimaryKey(Long oid);

    int insert(AssessRecord record);

    int insertSelective(AssessRecord record);

    AssessRecord selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(AssessRecord record);

    int updateByPrimaryKey(AssessRecord record);

    /**
     *
     * @param assessSubmit
     * @return
     */
    ResponseModel saveOrUpdate(CusAssessSubmit assessSubmit);

    /**
     *
     * @param teacherSubmit
     * @return
     */
    ResponseModel saveOrUpdateTeacher(CusTeacherSubmit teacherSubmit);

    /**
     *
     * @param yearId
     * @param deptId
     * @param key
     * @return
     */
    ResponseModel findStaffData(Long yearId, Long deptId, String key);

    /**
     *
     * @param record
     * @return
     */
    ResponseModel adjustScore(AssessRecord record);

    /**
     *
     * @param yearId
     * @return
     */
    ResponseModel findStaffCountData(Long yearId);

    /**
     *
     * @param yearId
     * @param deptId
     * @return
     */
    ResponseModel findScheduleData(Long yearId, Long deptId);

    /**
     *
     * @param yearId
     * @param assessType
     * @return
     */
    ResponseModel findScheduleComData(Long yearId, Integer assessType);

    /**
     *
     * @param yearId
     * @param userId
     * @param assessType
     * @return
     */
    ResponseModel findAssessItems(Long yearId, Long userId, Integer assessType);

    /**
     *
     * @param yearId
     * @param deptId
     * @param assessUser
     * @return
     */
    ResponseModel queryCanSubmit(Long yearId, Long deptId, Long assessUser);
}
