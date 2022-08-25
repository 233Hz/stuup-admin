package com.poho.stuup.api.controller;

import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Config;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.ISynchronizeService;
import com.poho.stuup.service.ITaskService;
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
    private ITaskService taskService;
    @Resource
    private IConfigService configService;
    @Resource
    private ISynchronizeService synchronizeService;


//    系统功能：1）发送信息的时间：8：00~16：00
//            2）时间开始自动发送短信提醒。（XX老师，您有考核打分任务需要处理。）
//            3）时间截止前三/一天自动发送短信提醒。（只发未完成的老师）（未完成：个人所有的评分表格提交）（XX老师，距离考核截止时间还有N天，请尽快处理。
//    @Scheduled(cron = "0/5 * * * * ?")
    /**
     * 定时计算。每天 13:00:00 执行一次
     */
    @Scheduled(cron = "0 0 13 * * ?")
    public void remind() {
        logger.info("----------------------------处理发送提醒短信开始----------------------------");
        try {
            String remindSwitch = "false";
            Config config = configService.selectByPrimaryKey(ProjectConstants.PARAM_REMIND_SWITCH);
            if (config != null && MicrovanUtil.isNotEmpty(config.getConfigValue())) {
                remindSwitch = config.getConfigValue();
            }
            logger.info("----------------------------开关状态：" + remindSwitch + "----------------------------");
            if ("true".equals(remindSwitch)) {
                taskService.remindStart();
                taskService.remindEnd();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("----------------------------处理发送提醒短信结束----------------------------");
    }

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
