package com.poho.stuup.dao;

import com.poho.stuup.model.ModelRule;

import java.util.List;

public interface ModelRuleMapper extends BaseDao<ModelRule> {

    List<ModelRule> queryAllList();
}