package com.poho.stuup.service;


import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Grade;

import java.util.List;

public interface IGradeService {
    int deleteByPrimaryKey(Integer oid);

    int insert(Grade record);

    int insertSelective(Grade record);

    Grade selectByPrimaryKey(Integer oid);

    int updateByPrimaryKeySelective(Grade record);

    int updateByPrimaryKey(Grade record);

    /**
     *
     * @return
     */
    List<Grade> findGrades();


    ResponseModel findDataPageResult(String key, Integer page, int pageSize);

    /**
     * 新增或修改年级
     *
     * @param grade
     * @return
     */
    ResponseModel saveOrUpdateGrade(Grade grade);
}
