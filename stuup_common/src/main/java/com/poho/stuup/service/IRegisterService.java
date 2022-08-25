package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Register;

/**
 * @Author wupeng
 * @Description 考核登记表逻辑处理接口
 * @Date 2020-09-22 9:15
 * @return
 */
public interface IRegisterService {
    int deleteByPrimaryKey(Long oid);

    int insert(Register record);

    int insertSelective(Register record);

    Register selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(Register record);

    int updateByPrimaryKey(Register record);

    /**
     *
     * @param userId
     * @param yearId
     * @param deptId
     * @param rangeDept
     * @param state
     * @param key
     * @param page
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String userId, String yearId, String deptId, String rangeDept, String state, String key, int page, int pageSize);

    /**
     *
     * @param yearId
     * @param userId
     * @return
     */
    ResponseModel findData(Long yearId, Long userId);

    /**
     *
     * @param register
     * @return
     */
    ResponseModel saveOrUpdate(Register register);

    /**
     *
     * @param ids
     * @param state
     * @return
     */
    ResponseModel batch(String ids, String state);

    /**
     *
     * @param oid
     * @return
     */
    ResponseModel findData(Long oid);

    /**
     *
     * @param register
     * @return
     */
    ResponseModel submitLeaderOpinion(Register register);
}
