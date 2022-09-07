package com.poho.stuup.dao;

import java.util.List;
import java.util.Map;
import com.poho.stuup.model.Class;

public interface ClassMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Class record);

    int insertSelective(Class record);

    Class selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Class record);

    int updateByPrimaryKey(Class record);

    /**
     * @param map
     * @return
     */
    List<Class> findAllClass(Map<String, Object> map);

    /**
     * 筛选后的班级个数
     *
     * @param map
     * @return
     */
    int findTotalClassByCond(Map<String, Object> map);

    /**
     * 筛选后的班级对象
     *
     * @param map
     * @return
     */
    List<Class> findClassPageResultByCond(Map<String, Object> map);

    /**
     * 根据年级查询班级
     *
     * @param grade
     * @return
     */
    List<Class> findClassByGrade(String grade);

    /**
     * 获取班级个数
     *
     * @return
     */
    int findClass(Map<String, Object> map);

    /**
     * 批量删除班级
     *
     * @return
     */
    int deleteClass(Map<String, Object> map);

    /**
     *
     * @param map
     * @return
     */
    Class findClassByName(Map<String, Object> map);

    /**
     *
     * @param map
     * @return
     */
    Class checkClass(Map<String, Object> map);

    /**
     *
     * @param classNo
     * @return
     */
    Class findClassByCode(String classNo);

    /**
     * 根据班主任查询班级
     * @param teacherId
     * @return
     */
    List<Class> findClassByAdmin(Integer teacherId);
}