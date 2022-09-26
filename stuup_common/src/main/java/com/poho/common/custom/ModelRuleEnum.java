package com.poho.common.custom;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.poho.stuup.model.ModelRule;
import com.poho.stuup.service.IModelRuleService;

import java.util.List;

public enum ModelRuleEnum {

    TOMATO("tomato")
    ,WINTERSWEET("wintersweet")
    ,DAISY("daisy" );

    ModelRuleEnum(String typeCode) {
        this.typeCode = typeCode;
    };
    private String typeCode;

    private Integer seedScore;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public Integer getSeedScore() {
        return seedScore;
    }

    public void setSeedScore(Integer seedScore) {
        this.seedScore = seedScore;
    }

    static {
        IModelRuleService modelRuleService = SpringUtil.getBean(IModelRuleService.class);
        List<ModelRule> list = modelRuleService.findAllList();
        if(CollUtil.isNotEmpty(list)){
            for (ModelRule modelRule : list) {
                if(TOMATO.getTypeCode().equals(modelRule.getTypeCode())){
                    TOMATO.setSeedScore(modelRule.getSeedScore());
                } else if(WINTERSWEET.getTypeCode().equals(modelRule.getTypeCode())){
                    WINTERSWEET.setSeedScore(modelRule.getSeedScore());
                } else if(DAISY.getTypeCode().equals(modelRule.getTypeCode())) {
                    DAISY.setSeedScore(modelRule.getSeedScore());
                }
            }
        }

    }
}
