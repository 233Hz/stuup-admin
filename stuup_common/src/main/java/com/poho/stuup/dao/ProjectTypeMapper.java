package com.poho.stuup.dao;

import com.poho.stuup.model.ProjectType;

import java.util.List;

public interface ProjectTypeMapper extends BaseDao<ProjectType> {

    List<ProjectType> queryAllList();

}