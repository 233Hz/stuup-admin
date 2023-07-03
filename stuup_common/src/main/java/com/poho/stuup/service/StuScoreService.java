package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.model.dto.GrowGardenDTO;
import com.poho.stuup.model.vo.GrowGardenVO;

import java.math.BigDecimal;

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
    void updateTotalScore(Long studentId, BigDecimal score);

    /**
     * @description: 查询各花园名单
     * @param: page
     * @param: query
     * @param: userId
     * @return: com.poho.common.custom.ResponseModel<com.baomidou.mybatisplus.core.metadata.IPage < com.poho.stuup.model.vo.GrowGardenVO>>
     * @author BUNGA
     * @date: 2023/6/9 10:02
     */
    ResponseModel<IPage<GrowGardenVO>> getGrowGarden(Page<GrowGardenVO> page, GrowGardenDTO query, Long userId);

}
