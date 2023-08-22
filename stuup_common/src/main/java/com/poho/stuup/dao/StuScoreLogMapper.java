package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.StuScoreLog;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 学生积分日志 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@Mapper
public interface StuScoreLogMapper extends BaseMapper<StuScoreLog> {

    IPage<StudentRecScoreVO> pageStudentRecScore(Page<StudentRecScoreVO> page, @Param("studentId") Long studentId);

    int executeSql(@Param("sql") String sql);
}
