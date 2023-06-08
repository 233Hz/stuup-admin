package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.SchoolClaRankDTO;
import com.poho.stuup.model.dto.SchoolStuRankDTO;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.SchoolClaRankVO;
import com.poho.stuup.model.vo.SchoolStuRankVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 积分记录表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface RecScoreService extends IService<RecScore> {

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecScoreVO>
     * @author BUNGA
     * @date: 2023/6/1 18:07
     */
    IPage<RecScoreVO> getRecScorePage(Page<RecScoreVO> page, RecScoreDTO query);

    /**
     * @description: 计算成长积分
     * @param: studentIds
     * @param: batchCode
     * @return: void
     * @author BUNGA
     * @date: 2023/5/29 13:08
     */
    void calculateScore(List<RecDefault> recDefaults, Long yearId, GrowthItem growthItem);

    /**
     * 查找该时间段内学生获取的分数
     *
     * @param growthId  成长项id
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return Map<学生id, 学生获得的分数>
     */
    Map<Long, Integer> findTimePeriodScoreMap(Long growthId, Date startTime, Date endTime);

    /**
     * @description: 分页查询全校学生排名
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage
     * @author BUNGA
     * @date: 2023/6/2 13:22
     */
    IPage<SchoolStuRankVO> getSchoolStuRank(Page<SchoolStuRankVO> page, SchoolStuRankDTO query);

    IPage<SchoolClaRankVO> getSchoolClaRank(Page<SchoolClaRankVO> page, SchoolClaRankDTO query);

    /**
     * @description: 计算成长积分
     * @param: periodEnum
     * @return: void
     * @author BUNGA
     * @date: 2023/6/6 10:30
     */
    void calculateScore(PeriodEnum periodEnum);
}
