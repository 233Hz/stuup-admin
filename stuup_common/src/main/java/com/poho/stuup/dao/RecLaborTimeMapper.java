package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecLaborTime;
import com.poho.stuup.model.dto.RecLaborTimeDTO;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.model.vo.RecLaborTimeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 生产劳动实践记录填报 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Mapper
public interface RecLaborTimeMapper extends BaseMapper<RecLaborTime> {

    IPage<RecLaborTimeVO> getRecLaborTimePage(Page<RecLaborTimeVO> page, @Param("query") RecLaborTimeDTO query);

    List<RecLaborTimeExcel> selectExportData(@Param("query") Map<String, Object> params);
}
