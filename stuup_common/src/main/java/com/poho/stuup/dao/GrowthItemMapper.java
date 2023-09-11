package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.GrowthItemSelectVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 成长项 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@Mapper
public interface GrowthItemMapper extends BaseMapper<GrowthItem> {

    List<GrowthItemSelectVO> getStudentGrowthItems();

    Long fetchMaxId();
}
