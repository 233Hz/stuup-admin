package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.Grade;
import com.poho.stuup.service.IGradeService;
import com.poho.stuup.util.ProjectUtil;
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
 * 年级信息维护
 * 提供对年级信息的新增、修改、删除、查询等功能
 * 删除属于逻辑删除
 */

@Api(tags = "年级信息相关接口")
@RestController
@RequestMapping("/grade")
public class GradeController {

    private final static Logger logger = LoggerFactory.getLogger(GradeController.class);

    @Autowired
    private IGradeService gradeService;

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel gradeList(String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return gradeService.findDataPageResult(key, page, pageSize);
    }

    @ApiImplicitParams({ @ApiImplicitParam(paramType = "header", dataType = "string", name = "Authorization", value = "登录成功获取的token", required = true) })
    @ApiOperation(value = "全部年级信息", httpMethod = "GET")
    @GetMapping("/all")
    public ResponseModel all() {
        return ResponseModel.newSuccessData(gradeService.findGrades());
    }

    /**
     *
     * @param grade
     * @return
     */
    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResponseModel saveOrUpdate(@RequestBody Grade grade) {
        return gradeService.saveOrUpdateGrade(grade);
    }

    @RequestMapping(value = "/delMultiGrade", method = RequestMethod.POST)
    public ResponseModel delMultiGrade(@RequestBody Map params) {
        //$$班级表、试卷管理表、学生表
        ResponseModel model = new ResponseModel();
        String ids = params.get("ids").toString();
        String[] idArr = ids.split(",");
        if (ids != null && idArr.length > 0) {
            for (String id : idArr) {
                gradeService.deleteByPrimaryKey(Integer.valueOf(id));
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("删除成功");
        } else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("请选择后点击删除");
        }
        return model;
    }
}
