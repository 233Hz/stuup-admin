package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.service.IProjectRuleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@Api(tags = "项目积分兑换规则相关接口")
@RestController
@RequestMapping("/projectRule")
public class ProjectRuleController {

    @Resource
    private IProjectRuleService projectRuleService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list(String key, String current, String size, Long typeId) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return projectRuleService.findDataPageResult(key, page, pageSize, typeId);
    }
}
