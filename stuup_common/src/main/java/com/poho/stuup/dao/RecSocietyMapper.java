package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecSociety;
import com.poho.stuup.model.dto.RecSocietyDTO;
import com.poho.stuup.model.vo.RecSocietyVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 参加社团记录填报 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Mapper
public interface RecSocietyMapper extends BaseMapper<RecSociety> {

    IPage<RecSocietyVO> getRecSocietyPage(Page<RecSocietyVO> page, @Param("query") RecSocietyDTO query);
}
