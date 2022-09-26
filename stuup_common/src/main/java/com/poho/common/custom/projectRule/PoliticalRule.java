package com.poho.common.custom.projectRule;

import com.poho.common.custom.ProjectRuleEnum;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Political;
import com.poho.stuup.model.ScoreDetail;

public class PoliticalRule implements IProjectRule<Political> {

    @Override
    public ScoreDetail handler(Political obj) {
        //TODO 后面优化 1条新增记录 1分
        int score = 1;
        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setStuId(obj.getStuId());
        scoreDetail.setScore(score);
        scoreDetail.setScoreType(ProjectConstants.SCORE_TYPE_ADD);
        scoreDetail.setRecordId(obj.getOid());
        scoreDetail.setRuleClass(ProjectRuleEnum.POLITICAL_RULE.getCode());
        scoreDetail.setDescription("党团活动信息得分:" + score);
        return scoreDetail;
    }
}
