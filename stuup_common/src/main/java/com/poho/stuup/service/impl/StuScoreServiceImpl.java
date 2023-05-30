package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.StuScoreMapper;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.service.StuScoreService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生积分表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Service
public class StuScoreServiceImpl extends ServiceImpl<StuScoreMapper, StuScore> implements StuScoreService {

    @Override
    public void updateTotalScore(Long studentId, Integer score) {
        StuScore stuScore = this.getOne(Wrappers.<StuScore>lambdaQuery().eq(StuScore::getStudentId, studentId));
        if (stuScore != null) {
            Integer totalScore = stuScore.getScore();
            totalScore += score;
            this.update(Wrappers.<StuScore>lambdaUpdate()
                    .set(StuScore::getScore, totalScore)
                    .eq(StuScore::getStudentId, studentId));
        } else {
            // 不存在则创建
            StuScore saveStuScore = new StuScore();
            saveStuScore.setStudentId(studentId);
            saveStuScore.setScore(score);
            this.save(saveStuScore);
        }
    }
}
