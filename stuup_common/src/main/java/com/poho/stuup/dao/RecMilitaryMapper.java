package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.model.dto.RecMilitaryDTO;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.model.vo.RecMilitaryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 军事训练记录填报 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Mapper
public interface RecMilitaryMapper extends BaseMapper<RecMilitary> {

    IPage<RecMilitaryVO> getRecMilitaryPage(Page<RecMilitaryVO> page, @Param("query") RecMilitaryDTO query);

    List<RecMilitaryExcel> selectExportData(@Param("query") Map<String, Object> params);
}
