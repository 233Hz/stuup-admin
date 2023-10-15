package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.RecProject;
import com.poho.stuup.model.excel.RecProjectExcel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参加项目记录表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-10-13
 */
@Mapper
public interface RecProjectMapper extends BaseMapper<RecProject> {

    List<RecProjectExcel> selectExportData(@Param("query") Map<String, Object> params);
}
