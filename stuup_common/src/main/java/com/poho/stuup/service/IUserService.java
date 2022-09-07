package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.User;

import java.util.List;
import java.util.Map;

/**
 * @Author wupeng
 * @Description 用户处理接口
 * @Date 2020-07-22 21:32
 * @return
 */
public interface IUserService {
    int deleteByPrimaryKey(Long oid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long oid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);


    ResponseModel checkLogin(String loginName, String password);

    /**
     *
     * @param key
     * @param state
     * @param pageInt
     * @param pageSize
     * @return
     */
    ResponseModel findDataPageResult(String key, String state, int pageInt, int pageSize);

    /**
     *
     * @param user
     * @return
     */
    ResponseModel saveOrUpdate(User user);

    /**
     *
     * @param ids
     * @return
     */
    ResponseModel del(String ids);

    /**
     *
     * @return
     */
    ResponseModel queryList();

    /**
     *
     * @param userId
     * @param params
     * @return
     */
    ResponseModel updatePassword(String userId, Map params);


    /**
     *
     * @param oid
     * @param yearId
     * @return
     */
    ResponseModel findUserData(Long oid, Long yearId);

    /**
     *
     * @param userList
     * @return
     */
    Map<String, Object> importUserList(List<User> userList);

}
