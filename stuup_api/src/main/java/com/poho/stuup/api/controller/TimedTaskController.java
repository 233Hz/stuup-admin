package com.poho.stuup.api.controller;

import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Config;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.ISynchronizeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author: wupeng
 * @Description: 定时任务
 * @Date: Created in 15:10 2020/10/21
 * @Modified By:
 */
@Component
public class TimedTaskController {
    private static final Logger logger = LoggerFactory.getLogger(TimedTaskController.class);
    @Resource
    private IConfigService configService;
    @Resource
    private ISynchronizeService synchronizeService;

    /**
     * 定时计算。每天晚上 00:10:00 执行一次
     */
    @Scheduled(cron = "0 10 0 * * ?")
    public void go() {
        try {
            String syncSwitch = "false";
            Config config = configService.selectByPrimaryKey(ProjectConstants.PARAM_SYNC_SWITCH);
            if (config != null && MicrovanUtil.isNotEmpty(config.getConfigValue())) {
                syncSwitch = config.getConfigValue();
            }
            if ("true".equals(syncSwitch)) {
                //同步系部
                synchronizeService.synchronizeDept();
                //同步教师
                synchronizeService.synchronizeTeacher();
            }
        } catch (Exception e) {
            logger.info("自动同步报错", e);
        }
    }
}
