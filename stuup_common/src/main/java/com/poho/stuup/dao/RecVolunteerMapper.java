package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecVolunteer;
import com.poho.stuup.model.dto.RecVolunteerDTO;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.model.vo.RecVolunteerVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 志愿者活动记录填报 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Mapper
public interface RecVolunteerMapper extends BaseMapper<RecVolunteer> {

    IPage<RecVolunteerVO> getVolunteerPage(Page<RecVolunteerVO> page, @Param("query") RecVolunteerDTO query);

    List<RecVolunteerExcel> queryExcelList(@Param("query") Map<String, Object> params);
}
