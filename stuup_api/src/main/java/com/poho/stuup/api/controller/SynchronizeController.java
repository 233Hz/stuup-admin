package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.model.Config;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.ISynchronizeService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
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

                //同步部门
                synchronizeService.synchronizeDept();
                //同步系部
                synchronizeService.synchronizeFaculty();
                //同步教研组
                synchronizeService.synchronizeTeachGroup();
                //同步年级
                synchronizeService.synchronizeGrade();
                //同步学期
                synchronizeService.synchronizeTerm();
                //同步专业
                synchronizeService.synchronizeMajor();
                //同步班级
                synchronizeService.synchronizeClass();
                //同步教师
                synchronizeService.synchronizeTeacher();
                //同步学生
                synchronizeService.synchronizeStudent();

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

    @ApiOperation(value = "手动同步学生得分信息", httpMethod = "GET")
    @GetMapping("/syncStuScore")
    public ResponseModel syncStuScore (){
        ResponseModel model = new ResponseModel();
       StringBuilder sb = new StringBuilder();
        //同步学生信息初始化学生得分表信息
        sb.append(synchronizeService.syncStuInfoInitStuScore());
        //同步奖励信息

        //技能大赛信息
        //考证信息
        //军训信息
        //党团活动信息
        //志愿者服务信息
        model.setCode(CommonConstants.CODE_SUCCESS);
        model.setData(sb.toString());
        model.setMessage("同步成功");
        return model;
    }
}
