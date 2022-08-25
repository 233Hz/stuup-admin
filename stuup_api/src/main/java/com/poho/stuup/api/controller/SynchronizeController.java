package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Config;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.ISynchronizeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 11:13 2020/11/16
 * @Modified By:
 */
@RestController
@RequestMapping("/sync")
public class SynchronizeController {
    private static final Logger logger = LoggerFactory.getLogger(SynchronizeController.class);
    @Resource
    private IConfigService configService;
    @Resource
    private ISynchronizeService synchronizeService;

    /**
     * 手动同步
     * @return
     */
    @RequestMapping(value = "/go", method = RequestMethod.POST)
    public ResponseModel go() {
        ResponseModel model = new ResponseModel();
        try {
            String syncSwitch = "false";
            Config config = configService.selectByPrimaryKey(ProjectConstants.PARAM_SYNC_SWITCH);
            if (config != null && MicrovanUtil.isNotEmpty(config.getConfigValue())) {
                syncSwitch = config.getConfigValue();
            }
            if ("true".equals(syncSwitch)) {
                logger.info("----------------------------手动同步开始----------------------------");
                //同步系部
                synchronizeService.synchronizeDept();
                //同步教师
                synchronizeService.synchronizeTeacher();
                logger.info("----------------------------手动同步结束----------------------------");
                model.setCode(CommonConstants.CODE_SUCCESS);
                model.setMessage("同步成功");
            } else {
                model.setCode(CommonConstants.CODE_EXCEPTION);
                model.setMessage("同步开关未开启");
            }
        }
        catch (Exception e) {
            logger.info("手动同步报错", e);
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("同步失败，请稍后重试");
        }
        return model;
    }
}
