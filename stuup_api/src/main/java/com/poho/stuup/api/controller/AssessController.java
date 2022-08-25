package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusAssessSubmit;
import com.poho.stuup.custom.CusTeacherSubmit;
import com.poho.stuup.service.IAssessStateService;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.util.ProjectUtil;
import com.poho.stuup.service.IAssessRecordService;
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

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 13:53 2020/9/3
 * @Modified By:
 */
@Api(tags = "评分相关接口")
@RestController
@RequestMapping("/assess")
public class AssessController {
    @Resource
    private IAssessRecordService assessRecordService;
    @Resource
    private IUserService userService;
    @Resource
    private IAssessStateService assessStateService;
    @Resource
    private HttpServletRequest request;

    /**
     * 查询考核状态
     * @return
     */
    @RequestMapping(value = "/state", method = RequestMethod.GET)
    public ResponseModel state(Long yearId, String assessType) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        return assessStateService.findAssessState(yearId, Long.valueOf(sesUser), assessType);
    }

    /**
     * 查询中层用户
     * @param yearId
     * @param deptId
     * @param assessType
     * @return
     */
    @RequestMapping(value = "/middleUsers", method = RequestMethod.GET)
    public ResponseModel middleUsers(Long yearId, Long deptId, Integer assessType) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        return userService.findAssessMiddleUsers(yearId, deptId, assessType, Long.valueOf(sesUser));
    }

    /**
     * 查询校级领导分管中层用户
     * @param userRangeId
     * @return
     */
    @RequestMapping(value = "/middleRangeUsers", method = RequestMethod.GET)
    public ResponseModel middleRangeUsers(Long userRangeId) {
        return userService.findAssessRangeMiddleUsers(userRangeId);
    }

    /**
     * 查询教师用户
     * @param yearId
     * @param deptId
     * @param assessType
     * @param userType
     * @return
     */
    @RequestMapping(value = "/teacherUsers", method = RequestMethod.GET)
    public ResponseModel teacherUsers(Long yearId, Long deptId, Integer assessType, Integer userType) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        return userService.findAssessTeachers(yearId, deptId, assessType, Long.valueOf(sesUser), userType);
    }

    /**
     * 保存评分
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "保存评分", httpMethod = "POST")
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel save(@RequestBody CusAssessSubmit assessSubmit) {
        String assessUser = ProjectUtil.obtainLoginUser(request);
        assessSubmit.setAssessUser(Long.valueOf(assessUser));
        return assessRecordService.saveOrUpdate(assessSubmit);
    }

    /**
     * 保存员工评分
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "保存员工评分", httpMethod = "POST")
    @RequestMapping(value = "/saveTeacher", method = RequestMethod.POST)
    public ResponseModel saveTeacher(@RequestBody CusTeacherSubmit teacherSubmit) {
        String assessUser = ProjectUtil.obtainLoginUser(request);
        teacherSubmit.setAssessUser(Long.valueOf(assessUser));
        return assessRecordService.saveOrUpdateTeacher(teacherSubmit);
    }

    /**
     * 查询教师评分是否可提交
     * @param yearId
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/queryCanSubmit", method = RequestMethod.GET)
    public ResponseModel queryCanSubmit(Long yearId, Long deptId) {
        String assessUser = ProjectUtil.obtainLoginUser(request);
        return assessRecordService.queryCanSubmit(yearId, deptId, Long.valueOf(assessUser));
    }
}
