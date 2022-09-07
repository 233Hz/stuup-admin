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


}