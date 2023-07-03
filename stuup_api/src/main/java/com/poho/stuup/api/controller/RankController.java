package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.RankMonthService;
import com.poho.stuup.service.RankSemesterService;
import com.poho.stuup.service.RankYearService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 排行榜 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-28
 */
@RestController
@RequestMapping("/rank")
public class RankController {

    @Resource
    private RankYearService rankYearService;

    @Resource
    private RankSemesterService rankSemesterService;

    @Resource
    private RankMonthService rankMonthService;


    /**
     * 全校排名（实时）
     */
    @GetMapping("/realTime/school")
    public ResponseModel<List<YearRankVO>> getSchoolRank() {
        return ResponseModel.ok(rankYearService.getSchoolRank());
    }

    /**
     * 班级排名（实时）
     */
    @GetMapping("/realTime/class")
    public ResponseModel<List<ClassRankVO>> getClassRank() {
        return ResponseModel.ok(rankYearService.getClassRank());
    }

    /**
     * 专业排名（实时）
     */
    @GetMapping("/realTime/major")
    public ResponseModel<List<MajorRankVO>> getMajorRank() {
        return ResponseModel.ok(rankYearService.getMajorRank());
    }

    /**
     * 系部排名（实时）
     */
    @GetMapping("/realTime/faculty")
    public ResponseModel<List<FacultyRankVO>> getFacultyRank() {
        return ResponseModel.ok(rankYearService.getFacultyRank());
    }

    /**
     * 进步榜
     */
    @GetMapping("/progress")
    public ResponseModel<List<ProgressRankVO>> getProgressRank() {
        return ResponseModel.ok(rankMonthService.getProgressRank());
    }

}
