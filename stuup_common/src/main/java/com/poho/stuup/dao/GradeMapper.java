package com.poho.stuup.dao;

import com.poho.stuup.model.Grade;

import java.util.List;
import java.util.Map;

public interface GradeMapper {
    int deleteByPrimaryKey(Integer oid);

    int insert(Grade record);

    int insertSelective(Grade record);

    Grade selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(Grade record);

    int updateByPrimaryKey(Grade record);

    /**
     * @return
     */
    List<Grade> findGrades();

    /**
     * 筛选后的年级个数
     *
     * @param map
     * @return
     */
    int findTotalGradeByCond(Map<String, Object> map);

    /**
     * 筛选后的年级对象
     *
     * @param map
     * @return
     */
    List<Grade> findGradePageResultByCond(Map<String, Object> map);

    /**
     * @param grade
     * @return
     */
    Grade checkGrade(Grade grade);

    /**
     * @param year
     * @return
     */
    Grade findGradeByYear(String year);

    List<Grade> selectAll();
}