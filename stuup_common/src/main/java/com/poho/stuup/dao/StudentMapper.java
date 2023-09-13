package com.poho.stuup.dao;


import com.poho.stuup.model.Student;
import com.poho.stuup.model.dto.StudentIdAndUserIdDTO;
import com.poho.stuup.model.vo.MajorPopulationsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Student record);

    int insertSelective(Student record);

    Student selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Student record);

    int updateByPrimaryKey(Student record);

    /**
     * @param map
     * @return
     */
    Student selectByStudentNo(Map<String, Object> map);

    /**
     * @param classId
     * @return
     */
    int findTotalStudentByClass(Integer classId);

    /**
     * @param map
     * @return
     */
    int findTotalStudentByCond(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    List<Student> findStudentPageResultByCond(Map<String, Object> map);

    List<Student> findAllStudent(Map<String, Object> map);

    /**
     * 通过学号查找学生id
     *
     * @param studentNo 学号
     * @return studentId 学生id
     */
    Long getIdByStudentNo(@Param("studentNo") String studentNo);

    List<Long> selectIdList();

    /**
     * @description: 通过学号获取学生信息
     * @param: loginName
     * @return: com.poho.stuup.model.Student
     * @author BUNGA
     * @date: 2023/6/9 10:45
     */
    Student getStudentForStudentNO(@Param("studentNo") String studentNo);

    List<Student> getAllStudent();

    List<Integer> getAllStudentId();

    List<Long> findStudentUserIdByClassId(@Param("classIds") List<Integer> classIds);

    Integer countAtSchoolNum();

    List<MajorPopulationsVO> countMajorPopulations();

    /**
     * 统计性别人数
     *
     * @return
     */
    Integer countSexNum(@Param("sex") Integer sex);

    Integer getClassIdByStudentNo(@Param("studentNo") String studentNo);

    List<StudentIdAndUserIdDTO> selectAllStudentIdUserId();
}