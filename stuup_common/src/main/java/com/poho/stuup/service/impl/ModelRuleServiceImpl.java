package com.poho.stuup.service.impl;

import com.poho.stuup.dao.ModelRuleMapper;
import com.poho.stuup.model.ModelRule;
import com.poho.stuup.service.IModelRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ModelRuleServiceImpl implements IModelRuleService {

    @Resource
    private ModelRuleMapper  modelRuleMapper;

    @Override
    public List<ModelRule> findAllList() {
        return modelRuleMapper.queryAllList();
    }

}
