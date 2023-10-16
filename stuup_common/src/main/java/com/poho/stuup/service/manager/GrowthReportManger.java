package com.poho.stuup.service.manager;

import com.alibaba.fastjson.JSONObject;
import com.poho.stuup.constant.CacheKeyConstant;
import com.poho.stuup.model.Config;
import com.poho.stuup.service.IConfigService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class GrowthReportManger {

    private static final String GROWTH_REPORT_CONFIG_KEY = "growth_report_config";

    private final IConfigService configService;

    @Cacheable(value = CacheKeyConstant.GROWTH_REPORT_CONFIG, key = "#root.methodName")
    public Map getGrowthReportConfig() {
        Config config = configService.selectByPrimaryKey(GROWTH_REPORT_CONFIG_KEY);
        if (config == null) throw new RuntimeException("成长报告配置项不存在");
        String configValue = config.getConfigValue();
        return JSONObject.parseObject(configValue, Map.class);
    }

}
