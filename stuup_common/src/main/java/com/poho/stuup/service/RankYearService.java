package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RankYear;
import com.poho.stuup.model.vo.ClassRankVO;
import com.poho.stuup.model.vo.FacultyRankVO;
import com.poho.stuup.model.vo.MajorRankVO;
import com.poho.stuup.model.vo.YearRankVO;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 年度排行榜 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
public interface RankYearService extends IService<RankYear> {

    List<YearRankVO> getSchoolRank(Long yearId);

    List<ClassRankVO> getClassRank(Long yearId);

    List<MajorRankVO> getMajorRank(Long yearId);

    List<FacultyRankVO> getFacultyRank(Long yearId);

    void generateRank(Date date);
}
