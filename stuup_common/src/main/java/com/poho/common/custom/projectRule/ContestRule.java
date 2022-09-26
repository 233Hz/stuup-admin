package com.poho.common.custom.projectRule;

import cn.hutool.core.map.MapUtil;
import com.poho.common.custom.ProjectRuleEnum;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Contest;
import com.poho.stuup.model.ScoreDetail;

import java.util.Map;

public class ContestRule implements IProjectRule<Contest> {

    @Override
    public ScoreDetail handler(Contest obj) {
        //TODO 后面优化 级别1 4分， 级别2 3分 级别3 2分 级别4 1分
        Map<Integer, Integer> levelToScoreMap = MapUtil.newHashMap();
        levelToScoreMap.put(1, 4);
        levelToScoreMap.put(2, 3);
        levelToScoreMap.put(3, 2);
        levelToScoreMap.put(4, 1);
        int score = levelToScoreMap.get(obj.getLevel());
        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setStuId(obj.getStuId());
        scoreDetail.setScore(score);
        scoreDetail.setRecordId(obj.getOid());
        scoreDetail.setScoreType(ProjectConstants.SCORE_TYPE_ADD);
        scoreDetail.setRuleClass(ProjectRuleEnum.CONTEST_RULE.getCode());
        scoreDetail.setDescription("技能大赛信息得分:" + score);
        return scoreDetail;
    }
}
