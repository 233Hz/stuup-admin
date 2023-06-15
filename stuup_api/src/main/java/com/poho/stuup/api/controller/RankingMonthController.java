package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.ProgressRankDTO;
import com.poho.stuup.model.vo.ProgressRankVO;
import com.poho.stuup.service.RankingMonthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 学期排行榜 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@RestController
@RequestMapping("/rankingMonth")
public class RankingMonthController {

    @Resource
    private RankingMonthService rankingMonthService;

    @GetMapping("/progressRanking")
    public ResponseModel<List<ProgressRankVO>> getProgressRanking(ProgressRankDTO query) {
        return ResponseModel.ok(rankingMonthService.getProgressRanking(query));
    }

}
