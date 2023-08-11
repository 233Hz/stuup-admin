package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.Course;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 课程信息表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-08
 */
@Mapper
public interface CourseMapper extends BaseMapper<Course> {

}
