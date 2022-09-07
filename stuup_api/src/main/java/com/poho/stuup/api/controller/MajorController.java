package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.Major;
import com.poho.stuup.service.IFacultyService;
import com.poho.stuup.service.IMajorService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 专业信息维护
 * 提供对专业信息的新增、修改、查询、删除等功能
 */


@Api(tags = "专业信息相关接口")
@RestController
@RequestMapping("/major")
public class MajorController {
    private final static Logger logger = LoggerFactory.getLogger(MajorController.class);
    @Autowired
    private IMajorService majorService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size){
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return majorService.findPageResult(key, page, pageSize);
    }

    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
    public ResponseModel saveOrUpdate(@RequestBody Major major) {
        return majorService.saveOrUpdate(major);
    }

    @RequestMapping(value = "/delMultiMajor", method = RequestMethod.POST)
    public ResponseModel delMultiMajor(@RequestBody Map params) {
        //$$班级表、试卷管理表、学生表
        ResponseModel model = new ResponseModel();
        String ids = params.get("ids").toString();
        String[] idArr = ids.split(",");
        if (ids != null && idArr.length > 0) {
            for (String id : idArr) {
                majorService.deleteByPrimaryKey(Integer.valueOf(id));
            }
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("删除成功");
        }
        else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("请选择后点击删除");
        }
        return model;
    }
}
