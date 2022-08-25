package com.poho.stuup.dao;

import com.poho.stuup.model.Dept;

import java.util.Map;

public interface DeptMapper extends BaseDao<Dept> {
    /**
     *
     * @param param
     * @return
     */
    Dept checkDept(Map<String, Object> param);
}