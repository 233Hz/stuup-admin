package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.YearRankDTO;
import com.poho.stuup.model.vo.YearRankVO;
import com.poho.stuup.service.RankYearService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 年度排行榜 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@RestController
@RequestMapping("/rankingYear")
public class RankYearController {

    @Resource
    private RankYearService rankYearService;

    @GetMapping("/yearRank")
    public ResponseModel<IPage<YearRankVO>> getYearRank(Page<YearRankVO> page, @RequestParam("yearId") Long yearId, YearRankDTO query) {
//        return ResponseModel.ok(rankYearService.getYearRank(page, yearId, query));
        return null;
    }

}
