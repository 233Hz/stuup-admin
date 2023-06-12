package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.model.dto.ClassRankDTO;
import com.poho.stuup.model.dto.GrowGardenDTO;
import com.poho.stuup.model.dto.MajorRankDTO;
import com.poho.stuup.model.dto.SchoolRankDTO;
import com.poho.stuup.model.vo.ClassRankVO;
import com.poho.stuup.model.vo.GrowGardenVO;
import com.poho.stuup.model.vo.MajorRankVO;
import com.poho.stuup.model.vo.SchoolRankVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 学生积分表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Mapper
public interface StuScoreMapper extends BaseMapper<StuScore> {

    IPage<GrowGardenVO> getGrowGardenForStudent(Page<GrowGardenVO> page,
                                                @Param("query") GrowGardenDTO query,
                                                @Param("userClassId") Integer userClassId,
                                                @Param("startScore") Integer startScore,
                                                @Param("endScore") Integer endScore);

    IPage<SchoolRankVO> getSchoolRanking(Page<SchoolRankVO> page, @Param("query") SchoolRankDTO query);

    IPage<ClassRankVO> getClassRanking(Page<ClassRankVO> page, @Param("query") ClassRankDTO query);

    IPage<MajorRankVO> getMajorRanking(Page<MajorRankVO> page, @Param("query") MajorRankDTO query);
}
