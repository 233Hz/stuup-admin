package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.ModelRule;
import com.poho.stuup.service.IModelRuleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@Api(tags = "模型规则相关接口")
@RestController
@RequestMapping("/modelRule")
public class ModelRuleController {

    @Resource
    private IModelRuleService modelRuleService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel list() {
        ResponseModel model = new ResponseModel();
        List<ModelRule> list = modelRuleService.findAllList();
        if (MicrovanUtil.isNotEmpty(list)) {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("请求成功");
        } else {
            model.setCode(CommonConstants.CODE_SUCCESS);
            model.setMessage("无数据");
        }
        model.setData(list);
        return model;
    }
}
