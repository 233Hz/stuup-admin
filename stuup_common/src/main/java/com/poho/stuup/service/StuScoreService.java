package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.StuScore;

/**
 * <p>
 * 学生积分表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface StuScoreService extends IService<StuScore> {

    /**
     * 更新当前学生积分
     *
     * @param studentId
     * @param score
     */
    void updateTotalScore(Long studentId, Integer score);
}
