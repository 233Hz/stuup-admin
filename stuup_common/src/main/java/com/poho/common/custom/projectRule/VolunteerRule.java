package com.poho.common.custom.projectRule;

import com.poho.common.custom.ProjectRuleEnum;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.ScoreDetail;
import com.poho.stuup.model.Volunteer;


public class VolunteerRule implements IProjectRule<Volunteer> {

    @Override
    public ScoreDetail handler(Volunteer obj) {
        //TODO 服务时长 每4小时1分，小于4小时的1分
        int duration = obj.getDuration();
        int score = duration >= 4 ? duration/4 : 1;
        ScoreDetail scoreDetail = new ScoreDetail();
        scoreDetail.setStuId(obj.getStuId());
        scoreDetail.setScore(score);
        scoreDetail.setScoreType(ProjectConstants.SCORE_TYPE_ADD);
        scoreDetail.setRecordId(obj.getOid());
        scoreDetail.setRuleClass(ProjectRuleEnum.VOLUNTEER_RULE.getCode());
        scoreDetail.setDescription("志愿者服务信息得分:" + score);
        return scoreDetail;
    }
}
