package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;

import java.util.List;
import java.util.Map;
import com.poho.stuup.model.Class;

public interface IClassService {
    /**
     *
     * @param clazz
     */
    int updateByPrimaryKeySelective(Class clazz);

    /**
     * @param map
     * @return
     */
    List<Class> findAllClass(Map<String, Object> map);

    ResponseModel findDataPageResult( String key, int page, int pageSize, Integer facultyId, Integer gradeId);

    /**
     * 新增或修改学期
     *
     * @param clazz
     * @return
     */
    ResponseModel saveOrUpdate(Class clazz);

    /**
     * 获取该年级的班级
     *
     * @return
     */
    List<Class> findClassByGrade(String grade);

    /**
     * 批量删除班级
     *
     * @param ids
     * @return
     */
    boolean deleteClass(String ids);

    /**
     *
     * @param classes
     * @return
     */
    Map<String,Object> addImportClasses(List<Class> classes);

    /**
     *
     * @param classId
     * @return
     */
    Class findClassById(Integer classId);

    /**
     *
     * @param ids
     * @return
     */
    String findClassNamesByIds(String ids);

    List<Class> findClassByAdminTeacher(Integer id);
}
