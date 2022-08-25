package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.custom.CusMiddleRangeSubmit;
import com.poho.stuup.custom.CusRangeSubmit;
import com.poho.stuup.model.AssessRange;
import com.poho.stuup.service.IAssessRangeService;
import com.poho.stuup.service.IRangeMiddleService;
import com.poho.stuup.util.ProjectUtil;
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
 * @Description: 评分范围
 * @Date: Created in 14:05 2020/10/19
 * @Modified By:
 */
@RestController
@RequestMapping("/config")
public class AssessRangeController {
    @Resource
    private IAssessRangeService assessRangeService;
    @Resource
    private IRangeMiddleService rangeMiddleService;
    @Resource
    private HttpServletRequest request;

    /**
     *
     * @param key
     * @param current
     * @param size
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "获取评分范围", httpMethod = "GET")
    @RequestMapping(value = "/range", method = RequestMethod.GET)
    public ResponseModel range(String yearId, String deptId, String userType, String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return assessRangeService.findDataPageResult(yearId, deptId, userType, key, page, pageSize);
    }

    /**
     *
     * @param yearId
     * @param oid
     * @return
     */
    @RequestMapping(value = "/leaderRangeCanUsers", method = RequestMethod.GET)
    public ResponseModel leaderRangeCanUsers(Long yearId, Long oid) {
        return assessRangeService.findLeaderRangeSetUsers(yearId, oid);
    }

    /**
     *
     * @param yearId
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/rangeCanUsers", method = RequestMethod.GET)
    public ResponseModel rangeCanUsers(Long yearId, Long deptId) {
        return assessRangeService.findRangeSetUsers(yearId, deptId);
    }

    /**
     *
     * @param yearId
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/deptRangeCanUsers", method = RequestMethod.GET)
    public ResponseModel deptRangeCanUsers(Long yearId, Long deptId) {
        return assessRangeService.findDeptRangeSetUsers(yearId, deptId);
    }

    /**
     *
     * @param yearId
     * @param rangeId
     * @return
     */
    @RequestMapping(value = "/middleRangeCanUsers", method = RequestMethod.GET)
    public ResponseModel middleRangeCanUsers(Long yearId, Long rangeId) {
        return assessRangeService.findMiddleRangeSetUsers(yearId, rangeId);
    }

    /**
     * 保存范围配置
     * @param range
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "保存范围配置", httpMethod = "POST")
    @RequestMapping(value = "/saveRange", method = RequestMethod.POST)
    public ResponseModel saveRange(@RequestBody AssessRange range) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        range.setCreateUser(Long.valueOf(sesUser));
        return assessRangeService.saveOrUpdate(range);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/delRange", method = RequestMethod.POST)
    public ResponseModel delRange(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return assessRangeService.del(ids);
    }

    /**
     * 批量保存范围配置
     * @param rangeSubmit
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "批量保存范围配置", httpMethod = "POST")
    @RequestMapping(value = "/saveMultiRange", method = RequestMethod.POST)
    public ResponseModel saveMultiRange(@RequestBody CusRangeSubmit rangeSubmit) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        rangeSubmit.setCreateUser(Long.valueOf(sesUser));
        return assessRangeService.saveMultiRange(rangeSubmit);
    }

    /**
     * 批量保存范围配置
     * @param rangeSubmit
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "批量保存校领导评价范围配置", httpMethod = "POST")
    @RequestMapping(value = "/saveMiddleRange", method = RequestMethod.POST)
    public ResponseModel saveMiddleRange(@RequestBody CusMiddleRangeSubmit rangeSubmit) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        rangeSubmit.setCreateUser(Long.valueOf(sesUser));
        return rangeMiddleService.saveMultiRange(rangeSubmit);
    }
}
