package com.poho.common.custom.projectRule;

import cn.hutool.core.map.MapUtil;
import com.poho.common.custom.ProjectRuleEnum;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Certificate;
import com.poho.stuup.model.ScoreDetail;

import java.util.Map;

public class CertificateRule implements IProjectRule<Certificate> {

    @Override
    public ScoreDetail handler(Certificate obj) {
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
        scoreDetail.setScoreType(ProjectConstants.SCORE_TYPE_ADD);
        scoreDetail.setRecordId(obj.getOid());
        scoreDetail.setRuleClass(ProjectRuleEnum.CERTIFICATE_RULE.getCode());
        scoreDetail.setDescription("考证信息得分:" + score);
        return scoreDetail;
    }
}
