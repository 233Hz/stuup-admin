package com.poho.stuup.api.controller;

import cn.dev33.satoken.annotation.SaCheckSafe;
import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.Faculty;
import com.poho.stuup.service.IFacultyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 系部信息维护
 * 提供对系部信息的新增、修改、查询、删除等功能
 * 负责人非必填
 */

@Api(tags = "系部信息相关接口")
@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final static Logger logger = LoggerFactory.getLogger(FacultyController.class);
    @Autowired
    private IFacultyService facultyService;

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
        return facultyService.findDataPageResult(key, page, pageSize);
    }

    @ApiImplicitParams({@ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true)})
    @ApiOperation(value = "全部系部信息", httpMethod = "GET")
    @GetMapping("/all")
    public ResponseModel all() {
        return ResponseModel.newSuccessData(facultyService.findAllFaculty());
    }

    /**
     * @param faculty
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResponseModel saveOrUpdate(@RequestBody Faculty faculty) {

        return facultyService.saveOrUpdate(faculty);

    }

    @SaCheckSafe("faculty_del")
    @RequestMapping(value = "/delMultiFaculty", method = RequestMethod.POST)
    public ResponseModel delMultiFaculty(@RequestBody Map params) {
        //$$班级表、系部课程表、专业表、教师表
        ResponseModel model = new ResponseModel();
        String ids = params.get("ids").toString();
        String[] idArr = ids.split(",");
        if (ids != null && idArr.length > 0) {
            for (String id : idArr) {
                facultyService.deleteByPrimaryKey(Integer.valueOf(id));
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMsg("删除成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMsg("请选择后点击删除");
        }
        return model;
    }
}