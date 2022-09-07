package com.poho.stuup.api.controller;


import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.service.ITeacherService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 教师信息维护
 * 提供对教师信息的新增、修改、查询、删除等功能
 */

@Api(tags = "教师信息相关接口")
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    private final static Logger logger = LoggerFactory.getLogger(TeacherController.class);
    @Autowired
    private ITeacherService teacherService;


    /**
     *
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(Integer facultyId, String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return teacherService.findDataPageResult(facultyId, key, page, pageSize);
    }

    @RequestMapping(value = "/searchTeacher", method = RequestMethod.POST)
    public ResponseModel searchTeacher(@RequestBody Map params) {
        ResponseModel model = new ResponseModel();
        String key = params.get("key").toString();
        List<Map<String, Object>> list = teacherService.searchTeacher(key);
        if (list != null && list.size() > 0) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setData(list);
        }
        else {
            model.setCode(CommonConstants.CODE_EXCEPTION);
            model.setMessage("无教师信息");
        }
        return model;
    }

}
