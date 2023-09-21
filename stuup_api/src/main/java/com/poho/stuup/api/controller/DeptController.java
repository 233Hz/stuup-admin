package com.poho.stuup.api.controller;

import cn.dev33.satoken.annotation.SaCheckSafe;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.Dept;
import com.poho.stuup.service.IDeptService;
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
 * @Date: Created in 10:41 2019/11/30
 * @Modified By:
 */
@Api(tags = "部门信息相关接口")
@RestController
@RequestMapping("/dept")
public class DeptController {
    @Resource
    private IDeptService deptService;
    @Resource
    private HttpServletRequest request;

    /**
     * 获取所有部门
     *
     * @return
     */
    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true)})
    @ApiOperation(value = "获取所有部门", httpMethod = "GET")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return deptService.findDataPageResult(key, page, pageSize);
    }

    /**
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseModel all() {
        return deptService.findData();
    }

    /**
     * @param dept
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ResponseModel save(@RequestBody Dept dept) {
        String sesUser = ProjectUtil.obtainLoginUserId(request);
        dept.setCreateUser(Long.valueOf(sesUser));
        return deptService.saveOrUpdate(dept);
    }

    /**
     * @param params
     * @return
     */
    @SaCheckSafe("dept_del")
    @RequestMapping(value = "/del", method = RequestMethod.POST)
    public ResponseModel del(@RequestBody Map params) {
        String ids = params.get("ids").toString();
        return deptService.del(ids);
    }
}
