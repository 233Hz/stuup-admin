package com.poho.common.custom;

import com.poho.common.custom.projectRule.*;

public enum ProjectRuleEnum {

    REWARD_RULE("rewardRule","奖励信息", new RewardRule())
    ,CONTEST_RULE("contestRule","技能大赛信息", new ContestRule())
    ,CERTIFICATE_RULE("certificateRule","考证信息", new CertificateRule())
    ,MILITARY_RULE("militaryRule","军训信息", new MilitaryRule())
    ,POLITICAL_RULE("politicalRue","党团活动信息", new PoliticalRule())
    ,VOLUNTEER_RULE("volunteerRule","志愿者服务信息", new VolunteerRule());

    ProjectRuleEnum(String code, String name, IProjectRule projectRule) {
        this.code = code;
        this.name = name;
        this.projectRule = projectRule;
    };
    private String code;

    private String name;

    private IProjectRule projectRule;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IProjectRule getProjectRule() {
        return projectRule;
    }

    public void setProjectRule(IProjectRule projectRule) {
        this.projectRule = projectRule;
    }
}

