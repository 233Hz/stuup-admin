package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.service.IScoreService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "积分相关接口")
@RestController
@RequestMapping("/score")
public class ScoreController {

    @Resource
    private IScoreService scoreService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size, Integer facultyId, Integer gradeId) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        //return scoreService.findDataPageResult(key, page, pageSize, facultyId, gradeId);
        return null;
    }
}
