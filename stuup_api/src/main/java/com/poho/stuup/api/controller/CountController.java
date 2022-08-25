package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.AssessRecord;
import com.poho.stuup.model.AssessScore;
import com.poho.stuup.service.IAssessRecordService;
import com.poho.stuup.service.IAssessScoreService;
import com.poho.stuup.util.ProjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 13:53 2020/9/3
 * @Modified By:
 */
@Api(tags = "统计报表相关接口")
@RestController
@RequestMapping("/count")
public class CountController {
    @Resource
    private IAssessRecordService assessRecordService;
    @Resource
    private IAssessScoreService assessScoreService;
    @Resource
    private HttpServletRequest request;

    /**
     * 绩效考核总表（员工）
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "绩效考核总表", httpMethod = "GET")
    @RequestMapping(value = "/staffData", method = RequestMethod.GET)
    public ResponseModel staffData(Long yearId, Long deptId, String key) {
        return assessRecordService.findStaffData(yearId, deptId, key);
    }

    /**
     * 绩效考核总表（员工）统计
     * @return
     */
    @RequestMapping(value = "/staffCount", method = RequestMethod.GET)
    public ResponseModel staffCount(Long yearId) {
        return assessRecordService.findStaffCountData(yearId);
    }

    /**
     * 调整分数
     * @return
     */
    @RequestMapping(value = "/adjustScore", method = RequestMethod.POST)
    public ResponseModel adjustScore(@RequestBody AssessRecord record) {
        return assessRecordService.adjustScore(record);
    }

    /**
     * 中层干部考核表
     * @return
     */
    @RequestMapping(value = "/middleData", method = RequestMethod.GET)
    public ResponseModel middleData(Long yearId, Long deptId, String key) {
        return assessScoreService.findData(yearId, deptId, key);
    }

    /**
     * 分数明细
     * @return
     */
    @RequestMapping(value = "/assessItems", method = RequestMethod.GET)
    public ResponseModel assessItems(Long yearId, Long userId, Integer assessType) {
        return assessRecordService.findAssessItems(yearId, userId, assessType);
    }

    /**
     * 考核进度表
     * @return
     */
    @RequestMapping(value = "/schedule", method = RequestMethod.GET)
    public ResponseModel schedule(Long yearId, Long deptId) {
        return assessRecordService.findScheduleData(yearId, deptId);
    }

    /**
     * 考核进度表对比数据
     * @return
     */
    @RequestMapping(value = "/scheduleCom", method = RequestMethod.GET)
    public ResponseModel scheduleCom(Long yearId, Integer assessType) {
        return assessRecordService.findScheduleComData(yearId, assessType);
    }

    /**
     * 生成考核结果
     * @return
     */
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public ResponseModel generate(@RequestBody Map params) {
        String yearId = params.get("yearId").toString();
        String assessUser = ProjectUtil.obtainLoginUser(request);
        return assessScoreService.generateData(yearId, Long.valueOf(assessUser));
    }

    /**
     * 调整合计分数
     * @return
     */
    @RequestMapping(value = "/adjustAssessScore", method = RequestMethod.POST)
    public ResponseModel adjustAssessScore(@RequestBody AssessScore score) {
        return assessScoreService.adjustAssessScore(score);
    }

    /**
     * 调整群众测评分数
     * @return
     */
    @RequestMapping(value = "/adjustAssessQzcp", method = RequestMethod.POST)
    public ResponseModel adjustAssessQzcp(@RequestBody AssessScore score) {
        return assessScoreService.adjustAssessQzcp(score);
    }
}
