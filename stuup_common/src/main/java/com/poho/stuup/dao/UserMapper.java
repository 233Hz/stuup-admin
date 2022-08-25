package com.poho.stuup.dao;

import com.poho.stuup.model.User;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseDao<User> {
    /**
     *
     * @param param
     * @return
     */
    User checkUser(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<User> findAssessMiddleUsers(Map<String, Object> param);

    /**
     * 根据评价类型查询参评人数
     * @param param
     * @return
     */
    int findAssessUserTotal(Map<String, Object> param);

    /**
     * 查询可以设置范围的人员，如果传了dept_id，会把现有部门下的人查出来
     * @param param
     * @return
     */
    List<User> findRangeCanUsers(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<User> findLeaderRangeCanUsers(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<Long> findRangeUsers(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<User> findAssessTeachers(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<User> findRangeUserList(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    List<User> findAssessMiddleRangeUsers(Map<String, Object> param);

    /**
     *
     * @param param
     * @return
     */
    int findAssessZCYGTotal(Map<String, Object> param);
}