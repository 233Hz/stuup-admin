package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecNation;
import com.poho.stuup.model.dto.RecNationDTO;
import com.poho.stuup.model.excel.RecNationExcel;
import com.poho.stuup.model.vo.RecNationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 参加国防民防项目记录填报 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Mapper
public interface RecNationMapper extends BaseMapper<RecNation> {

    IPage<RecNationVO> getRecNationPage(Page<RecNationVO> page, @Param("query") RecNationDTO query);

    List<RecNationExcel> queryExcelList(@Param("query") Map<String, Object> params);
}
