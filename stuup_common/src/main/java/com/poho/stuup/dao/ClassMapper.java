package com.poho.stuup.dao;

import com.poho.stuup.model.Class;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
     * @param map
     * @return
     */
    Class findClassByName(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    Class checkClass(Map<String, Object> map);

    /**
     * @param classNo
     * @return
     */
    Class findClassByCode(String classNo);

    /**
     * 根据班主任查询班级
     *
     * @param teacherId
     * @return
     */
    List<Class> findClassByAdmin(Integer teacherId);

    /**
     * @description: 获取老师所属的班级id
     * @param: teacherId    教师id
     * @return: java.lang.Integer
     * @author BUNGA
     * @date: 2023/6/9 10:50
     */
    Integer getClassIdForTeacher(@Param("teacherId") Long teacherId);
}