package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.StuScoreMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.User;
import com.poho.stuup.model.vo.GrowthInfo;
import com.poho.stuup.service.GrowthInfoService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/8/9 17:13
 */

@Service
public class GrowthInfoServiceImpl implements GrowthInfoService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private StuScoreMapper stuScoreMapper;

    @Resource
    private RecScoreService recScoreService;

    @Override
    public ResponseModel<GrowthInfo> getInfo(Long userId) {
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ResponseModel.failed("未查询到您的用户信息");
        String studentNo = user.getLoginName();
        Student student = studentMapper.getStudentForStudentNO(studentNo);
        if (student == null) return ResponseModel.failed("未查询到您的学生信息");
        GrowthInfo result = new GrowthInfo();
        String studentName = student.getName();
        result.setStudentName(studentName);
        Integer studentId = student.getId();
        StuScore stuScore = stuScoreMapper.selectOne(Wrappers.<StuScore>lambdaQuery()
                .select(StuScore::getScore)
                .eq(StuScore::getStudentId, studentId));
        BigDecimal score = stuScore.getScore();
        result.setTotalScore(score);
        Integer ranking = recScoreService.getStudentNowRanking(Long.valueOf(studentId));
        result.setRanking(ranking);
        List<RecScore> recScores = recScoreService.list(Wrappers.<RecScore>lambdaQuery()
                .select(RecScore::getId, RecScore::getScore)
                .eq(RecScore::getState, WhetherEnum.NO.getValue())
                .eq(RecScore::getStudentId, studentId));
        result.setUnearnedPoints(recScores);
        return ResponseModel.ok(result);
    }
}
