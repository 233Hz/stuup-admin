package com.poho.common.custom.projectRule;

import cn.hutool.core.map.MapUtil;
import com.poho.common.custom.ProjectRuleEnum;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Military;
import com.poho.stuup.model.ScoreDetail;

import java.util.Map;

public class MilitaryRule implements IProjectRule<Military> {

    @Override
    public ScoreDetail handler(Military obj) {
        //TODO 后面优化 军训等级 0不合格 1分  1合格 2
        Map<Integer, Integer> levelToScoreMap = MapUtil.newHashMap();
        levelToScoreMap.put(0, 1);
        levelToScoreMap.put(1, 2);
        int score = levelToScoreMap.get(obj.getLevel());
        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setStuId(obj.getStuId());
        scoreDetail.setScore(score);
        scoreDetail.setScoreType(ProjectConstants.SCORE_TYPE_ADD);
        scoreDetail.setRecordId(obj.getOid());
        scoreDetail.setRuleClass(ProjectRuleEnum.MILITARY_RULE.getCode());
        scoreDetail.setDescription("军训信息得分:" + score);
        return scoreDetail;
    }
}
