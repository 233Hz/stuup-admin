package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.*;
import com.poho.stuup.service.RankMonthService;
import com.poho.stuup.service.RankService;
import com.poho.stuup.service.RankYearService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
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
    private RankMonthService rankMonthService;

    @Resource
    private RankService rankService;


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

    @GetMapping("/wholeSchoolTop10")
    public ResponseModel<List<WholeSchoolTop10VO>> getWholeSchoolTop10Ranking() {
        return ResponseModel.ok(rankService.getWholeSchoolTop10Ranking());
    }

    @GetMapping("/wholeClassTop10")
    public ResponseModel<List<WholeClassTop10VO>> getWholeClassTop10Ranking() {
        return ResponseModel.ok(rankService.getWholeClassTop10Ranking());
    }

    @GetMapping("/progressTop10")
    public ResponseModel<List<ProgressTop10VO>> getProgressTop10Ranking() {
        List<ProgressRankVO> progressRank = rankMonthService.getProgressRank();
        int size = progressRank.size();
        List<ProgressTop10VO> result = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (i + 1 > 10) break;
            ProgressRankVO progressRankVO = progressRank.get(i);
            String studentName = progressRankVO.getStudentName();
            String className = progressRankVO.getClassName();
            String classTeacher = progressRankVO.getClassTeacher();
            Integer ranking = progressRankVO.getRank();
            BigDecimal score = progressRankVO.getScore();
            Integer riseRanking = progressRankVO.getRankChange();
            ProgressTop10VO progressTop10VO = new ProgressTop10VO();
            progressTop10VO.setStudentName(studentName);
            progressTop10VO.setClassName(className);
            progressTop10VO.setClassTeacher(classTeacher);
            progressTop10VO.setRanking(ranking);
            progressTop10VO.setScore(score);
            progressTop10VO.setRiseRanking(riseRanking);
            result.add(progressTop10VO);
        }
        return ResponseModel.ok(result);
    }

}
