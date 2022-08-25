package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.AssessScale;
import com.poho.stuup.service.IAssessScaleService;
import com.poho.stuup.util.ProjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 08:55 2020/9/18
 * @Modified By:
 */
@Api(tags = "评分比例配置")
@RestController
@RequestMapping("/config")
public class AssessScaleController {
    @Resource
    private IAssessScaleService assessScaleService;
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
    @ApiOperation(value = "获取所有评分比例配置信息", httpMethod = "GET")
    @RequestMapping(value = "/scale", method = RequestMethod.GET)
    public ResponseModel scale(String yearId, String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return assessScaleService.findDataPageResult(yearId, key, page, pageSize);
    }

    /**
     *
     * @param yearId
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/scaleData", method = RequestMethod.GET)
    public ResponseModel scaleData(Long yearId, Long deptId) {
        return assessScaleService.findData(yearId, deptId);
    }

    /**
     * 保存比例配置
     * @param scale
     * @return
     */
    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "保存比例配置", httpMethod = "POST")
    @RequestMapping(value = "/saveScale", method = RequestMethod.POST)
    public ResponseModel saveScale(@RequestBody AssessScale scale) {
        String sesUser = ProjectUtil.obtainLoginUser(request);
        scale.setCreateUser(Long.valueOf(sesUser));
        return assessScaleService.saveOrUpdate(scale);
    }

    /**
     * @param params
     * @return
     */
    @RequestMapping(value = "/delScale", method = RequestMethod.POST)
    public ResponseModel delScale(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return assessScaleService.del(ids);
    }

    /**
     * @param yearId
     * @return
     */
    @RequestMapping(value = "/initScale/{yearId}", method = RequestMethod.POST)
    public ResponseModel initScale(@PathVariable Long yearId) {
        return assessScaleService.init(yearId);
    }
}
