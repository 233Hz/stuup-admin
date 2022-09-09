package com.poho.stuup.dao;


import com.poho.stuup.model.TeachGroup;

import java.util.List;
import java.util.Map;

public interface TeachGroupMapper {
    int deleteByPrimaryKey(Integer oid);

    int insert(TeachGroup record);

    int insertSelective(TeachGroup record);

    TeachGroup selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(TeachGroup record);

    int updateByPrimaryKey(TeachGroup record);

    int findTotalTeachGroupByCond(Map<String, Object> map);

    List<TeachGroup> findTeachGroupPageResultByCond(Map<String, Object> map);

    List<TeachGroup> findAllTeachGroup();

    /**
     *
     * @param teachGroup
     * @return
     */
    TeachGroup checkTeachGroup(TeachGroup teachGroup);

    /**
     *
     * @param teacherId
     * @return
     */
    List<TeachGroup> findTeachGroupByAdmin(Integer teacherId);

    TeachGroup findTeachGroupByName(String groupName);

    List<TeachGroup> findTeachGroupByFacultyId(String facultyId);
}