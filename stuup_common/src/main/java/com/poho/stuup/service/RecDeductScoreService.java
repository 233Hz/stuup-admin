package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RecDeductScore;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;

/**
 * <p>
 * 扣分记录 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-18
 */
public interface RecDeductScoreService extends IService<RecDeductScore> {

    IPage<RecScoreVO> pageRecDeductScore(Page<RecScoreVO> page, RecScoreDTO query);
}
