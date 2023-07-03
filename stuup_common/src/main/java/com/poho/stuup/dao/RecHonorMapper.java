package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecHonor;
import com.poho.stuup.model.dto.RecHonorDTO;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.vo.RecHonorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 人荣誉记录填报 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Mapper
public interface RecHonorMapper extends BaseMapper<RecHonor> {

    IPage<RecHonorVO> getRecHonorPage(Page<RecHonorVO> page, @Param("query") RecHonorDTO query);

    List<RecHonorExcel> queryExcelList(@Param("query") Map<String, Object> params);
}
