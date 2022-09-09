package com.poho.stuup.service;


import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.TeachGroup;

import java.util.List;

/**
 * Create by 韩超军
 * 2018/4/2 下午4:30
 */
public interface ITeachGroupService {

    int deleteByPrimaryKey(Integer oid);

    int insert(TeachGroup record);

    int insertSelective(TeachGroup record);

    TeachGroup selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(TeachGroup record);

    int updateByPrimaryKey(TeachGroup record);


    ResponseModel findDataPageResult( Integer facultyId, String key, Integer page, int pageSize);

    ResponseModel saveOrUpdateTeachGroup(TeachGroup teachGroup);

    List<TeachGroup> findAllTeachGroup();

    TeachGroup findTeachGroupByName(String groupName);

    List<TeachGroup> findTeachGroupByFacultyId(String facultyId);
}
