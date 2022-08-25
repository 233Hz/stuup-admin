package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Config;

/**
 * @Author wupeng
 * @Description 系统配置参数接口
 * @Date 2020-08-07 21:59
 * @return
 */
public interface IConfigService {
    int deleteByPrimaryKey(String configKey);

    int insert(Config record);

    int insertSelective(Config record);

    Config selectByPrimaryKey(String configKey);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);

    /**
     *
     * @param key
     * @param value
     * @return
     */
    ResponseModel saveOrUpdate(String key, String value);
}
