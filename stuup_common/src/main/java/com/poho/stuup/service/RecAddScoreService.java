package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecAddScore;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.StudentRecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.model.vo.UnCollectScore;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 积分记录表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface RecAddScoreService extends IService<RecAddScore> {

    /**
     * @description: 分页查询
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.RecScoreVO>
     * @author BUNGA
     * @date: 2023/6/1 18:07
     */
    IPage<RecScoreVO> pageRecAddScore(Page<RecScoreVO> page, RecScoreDTO query);

    /**
     * @description: 查询学生积分获取记录（分页）
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.StudentRecScoreVO>
     * @author BUNGA
     * @date: 2023/6/16 17:04
     */
    ResponseModel<IPage<StudentRecScoreVO>> pageStudentRecScore(Page<StudentRecScoreVO> page, Long userId, StudentRecScoreDTO query);

    void batchCalculateScore(List<Long> studentIds, GrowthItem growthItem, Long yearId, Long semesterId, Date time);

    /**
     * @description: 计算该学生积分
     * @param: growthItemId
     * @param: studentId
     * @return: void
     * @author BUNGA
     * @date: 2023/6/16 15:08
     */
    void calculateStudentScore(Long growId, Long studentId);


    /**
     * 获取学生的当前排名
     *
     * @param studentId
     * @return
     */
    Integer getStudentNowRanking(Long studentId);

    void collectionTimeoutScore(Integer timeout);

    List<UnCollectScore> getUnCollectScore(Long userId);
}
