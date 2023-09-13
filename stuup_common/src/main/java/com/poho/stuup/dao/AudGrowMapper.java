package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.AudGrow;
import com.poho.stuup.model.dto.CountStudentApplyDTO;
import com.poho.stuup.model.dto.GrowRecordDTO;
import com.poho.stuup.model.vo.AudGrowthVO;
import com.poho.stuup.model.vo.ReviewOfEachClassVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 成长项目审核记录 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Mapper
public interface AudGrowMapper extends BaseMapper<AudGrow> {

    IPage<AudGrowthVO> pageAud(Page<AudGrowthVO> page, @Param("query") GrowRecordDTO query);

    int updateState(@Param("id") Long id, @Param("state") Integer state);

    List<ReviewOfEachClassVO> countReviewOfEachClass(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int countApplyInRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int countAuditInRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<CountStudentApplyDTO> countStudentApply(@Param("query") Map<String, Object> query);
}
