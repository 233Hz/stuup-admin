package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.model.dto.RecCaucusDTO;
import com.poho.stuup.model.vo.RecCaucusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 参加党团学习项目记录填报 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Mapper
public interface RecCaucusMapper extends BaseMapper<RecCaucus> {

    IPage<RecCaucusVO> getRecCaucusPage(Page<RecCaucusVO> page, @Param("query") RecCaucusDTO query);
}
