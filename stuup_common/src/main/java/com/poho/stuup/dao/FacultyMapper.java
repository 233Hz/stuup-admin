package com.poho.stuup.dao;

import com.poho.stuup.model.Faculty;

import java.util.List;
import java.util.Map;

public interface FacultyMapper {
    int deleteByPrimaryKey(Integer oid);

    int insert(Faculty record);

    int insertSelective(Faculty record);

    Faculty selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(Faculty record);

    int updateByPrimaryKey(Faculty record);

    /**
     * @return
     */
    List<Faculty> findAllFaculty();

    /**
     * @param map
     * @return
     */
    int findTotalFacultyByCond(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Faculty> findFacultyPageResultByCond(Map<String, Object> map);

    /**
     * @param faculty
     * @return
     */
    Faculty checkFaculty(Faculty faculty);

    /**
     * @param facultyName
     * @return
     */
    Faculty findFacultyByName(String facultyName);

    /**
     * @param teacherId
     * @return
     */
    List<Faculty> findFacultyByAdmin(Integer teacherId);

    List<Faculty> selectAll();
}