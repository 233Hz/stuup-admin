package com.poho.stuup.api.controller;

import com.poho.stuup.service.RankSemesterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 学期榜 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-28
 */
@RestController
@RequestMapping("/rankSemester")
public class RankSemesterController {


    @Resource
    private RankSemesterService rankSemesterService;


}
