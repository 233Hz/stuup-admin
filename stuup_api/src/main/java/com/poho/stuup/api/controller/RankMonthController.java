package com.poho.stuup.api.controller;

import com.poho.stuup.service.RankMonthService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
public class RankMonthController {

    @Resource
    private RankMonthService rankMonthService;

}
