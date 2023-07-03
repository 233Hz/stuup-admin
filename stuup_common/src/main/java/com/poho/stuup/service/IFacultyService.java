package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Faculty;

import java.util.List;
import java.util.Map;

public interface IFacultyService {
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

    ResponseModel findDataPageResult(String key, int page, int pageSize);

    /**
     * @param faculty
     * @return
     */
    ResponseModel saveOrUpdate(Faculty faculty);

    Faculty findFacultyByName(String facultyName);

    List<Faculty> findFacultyByAdmin(Integer id);

    Map<Integer, Faculty> facultyMap();
}
