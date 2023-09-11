package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecDeductScore;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * <p>
 * 扣分记录 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-18
 */
@Mapper
public interface RecDeductScoreMapper extends BaseMapper<RecDeductScore> {

    BigDecimal fetchTotalScore(@Param("studentId") Integer studentId, @Param("yearId") Long yearId, @Param("semesterId") Long semesterId);

    IPage<RecScoreVO> pageRecDeductScore(Page<RecScoreVO> page, RecScoreDTO query);
}
