package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.dto.GrowSearchDTO;
import com.poho.stuup.model.dto.RecDefaultDTO;
import com.poho.stuup.model.vo.GrowRecordVO;
import com.poho.stuup.model.vo.RecDefaultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 默认积分记录表（除综评表） Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
@Mapper
public interface RecDefaultMapper extends BaseMapper<RecDefault> {

    IPage<GrowRecordVO> growthRecordPage(Page page, @Param("query") GrowSearchDTO query);

    List<RecDefaultVO> growthRecordDetails(@Param("query") RecDefaultDTO query);
}
