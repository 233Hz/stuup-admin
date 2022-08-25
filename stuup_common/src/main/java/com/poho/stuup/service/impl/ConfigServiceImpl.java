package com.poho.stuup.service.impl;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.dao.ConfigMapper;
import com.poho.stuup.model.Config;
import com.poho.stuup.service.IConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author wupeng
 * @Description 系统配置参数接口实现类
 * @Date 2020-08-07 22:10
 * @return
 */
@Service
public class ConfigServiceImpl implements IConfigService {
    @Resource
    private ConfigMapper configMapper;

    @Override
    public int deleteByPrimaryKey(String configKey) {
        return configMapper.deleteByPrimaryKey(configKey);
    }

    @Override
    public int insert(Config record) {
        return configMapper.insert(record);
    }

    @Override
    public int insertSelective(Config record) {
        return configMapper.insertSelective(record);
    }

    @Override
    public Config selectByPrimaryKey(String configKey) {
        return configMapper.selectByPrimaryKey(configKey);
    }

    @Override
    public int updateByPrimaryKeySelective(Config record) {
        return configMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(Config record) {
        return configMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResponseModel saveOrUpdate(String key, String value) {
        ResponseModel model = new ResponseModel();
        int line;
        Config config = configMapper.selectByPrimaryKey(key);
        if (MicrovanUtil.isNotEmpty(config)) {
            config.setConfigValue(value);
            line = configMapper.updateByPrimaryKeySelective(config);
        } else {
            config = new Config();
            config.setConfigKey(key);
            config.setConfigValue(value);
            line = configMapper.insertSelective(config);
        }
        if (line > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("保存成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("保存失败，请稍后重试");
        }
        return model;
    }
}
