package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RankSemesterMapper;
import com.poho.stuup.dao.RecAddScoreMapper;
import com.poho.stuup.dao.SemesterMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.RankSemester;
import com.poho.stuup.model.RecAddScore;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Student;
import com.poho.stuup.service.RankSemesterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 学期榜 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-28
 */
@Slf4j
@Service
public class RankSemesterServiceImpl extends ServiceImpl<RankSemesterMapper, RankSemester> implements RankSemesterService {

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private RecAddScoreMapper recAddScoreMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateRank(Date date) {
        log.info("====================开始统计学期排行榜====================");
        long start = System.currentTimeMillis();

        Semester semester = semesterMapper.findByRange(date);
        if (semester == null) return;
        Long semesterId = semester.getId();

        // 所有学生
        List<Student> students = studentMapper.getAllStudent();

        // 查询出该学期的所有记录
        List<RecAddScore> recAddScores = recAddScoreMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getStudentId, RecAddScore::getScore)
                .eq(RecAddScore::getSemesterId, semesterId));
        int recordSize = recAddScores.size();

        // 对学生id进行分组求和
        Map<Long, BigDecimal> recScoresSumScore = recAddScores.stream().collect(Collectors.groupingBy(RecAddScore::getStudentId, Collectors.reducing(BigDecimal.ZERO, RecAddScore::getScore, BigDecimal::add)));

        List<RankSemester> rankSemesterList = students.stream().map(student -> {
                    RankSemester rankSemester = new RankSemester();
                    rankSemester.setSemesterId(semesterId);
                    rankSemester.setStudentId(student.getId().longValue());
                    rankSemester.setScore(recScoresSumScore.getOrDefault(student.getId().longValue(), BigDecimal.ZERO));
                    return rankSemester;
                })
                .collect(Collectors.toList());

        students.clear();
        recAddScores.clear();
        recScoresSumScore.clear();

        if (recordSize > 0) {
            rankSemesterList.sort(Comparator.comparing(RankSemester::getScore).reversed());
        }

        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = rankSemesterList.get(0).getScore();

        for (RankSemester rankSemester : rankSemesterList) {
            if (lastStudentScore.compareTo(rankSemester.getScore()) != 0) rank++;
            rankSemester.setRanking(rank);
            lastStudentScore = rankSemester.getScore();
            baseMapper.insert(rankSemester);
        }

        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }
}
