package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.dto.SemesterDTO;
import com.poho.stuup.model.vo.SemesterVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * <p>
 * 学期管理表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@Mapper
public interface SemesterMapper extends BaseMapper<Semester> {

    IPage<SemesterVO> pageSemester(Page<SemesterVO> page, @Param("query") SemesterDTO query);

    Long getCurrentSemesterId();

    Semester findTimeBelongYear(@Param("date") Date date);

    String getCurrTermName();

    Semester findByRange(@Param("date") Date date);
}
