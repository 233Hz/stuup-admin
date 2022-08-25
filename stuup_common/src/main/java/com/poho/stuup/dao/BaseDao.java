package com.poho.stuup.dao;

import java.util.List;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description: 基础Dao
 * @Date: Created in 13:00 2018/6/24
 * @Modified By:
 */
public interface BaseDao<T> {
    int deleteByPrimaryKey(Object oid);

    int insert(T t);

    int insertSelective(T t);

    T selectByPrimaryKey(Object oid);

    int updateByPrimaryKeySelective(T t);

    int updateByPrimaryKey(T t);

    /**
     * 批量删除
     * @param id
     * @return
     */
    int deleteBatch(Object[] id);

    /**
     * 根据条件查询列表
     * @param map
     * @return
     */
    List<T> queryList(Map<String, Object> map);

    /**
     * 根据条件查询所有
     * @param map
     * @return
     */
    int queryTotal(Map<String, Object> map);
}
