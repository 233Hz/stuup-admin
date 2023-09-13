package com.poho.stuup.dao;


import com.poho.stuup.model.Major;

import java.util.List;
import java.util.Map;

public interface MajorMapper {
    int deleteByPrimaryKey(Integer oid);

    int insert(Major record);

    int insertSelective(Major record);

    Major selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(Major record);

    int updateByPrimaryKey(Major record);

    /**
     * 筛选后专业的个数
     *
     * @param map
     * @return
     */
    int findTotalMajorByCond(Map<String, Object> map);

    /**
     * 筛选后的专业对象
     *
     * @param map
     * @return
     */
    List<Major> findMajorPageResultByCond(Map<String, Object> map);

    /**
     * @param major
     * @return
     */
    Major checkMajor(Major major);

    /**
     * @return
     */
    List<Major> findMajors();

    /**
     * @param majorName
     * @return
     */
    Major findMajorByName(String majorName);

    List<Major> selectAll();

    /**
     * 统计专业总数
     *
     * @return
     */
    int countMajorTotal();

    List<Major> selectAllIdName();
}