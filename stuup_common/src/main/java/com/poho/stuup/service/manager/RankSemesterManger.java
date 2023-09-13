package com.poho.stuup.service.manager;

import com.poho.stuup.dao.RankSemesterMapper;
import com.poho.stuup.model.RankSemester;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RankSemesterManger {

    private final RankSemesterMapper rankSemesterMapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveRank(List<RankSemester> rankSemesterList) {
        int rank = 1;
        BigDecimal lastStudentScore = rankSemesterList.get(0).getScore();

        for (RankSemester rankSemester : rankSemesterList) {
            if (lastStudentScore.compareTo(rankSemester.getScore()) != 0) rank++;
            rankSemester.setRanking(rank);
            lastStudentScore = rankSemester.getScore();
            rankSemesterMapper.insert(rankSemester);
        }
    }
}
