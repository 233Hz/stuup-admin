package com.poho.stuup.api.controller;

import com.poho.stuup.custom.CusMsg;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.service.IMsgService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 14:02 2019/12/07
 * @Modified By:
 */
@Api(tags = "发送短信相关接口")
@RestController
@RequestMapping("/msg")
public class MsgController {
    @Resource
    private IMsgService msgService;

    /**
     *
     * @return
     */
    @ApiOperation(value = "发送登录验证码", httpMethod = "POST")
    @RequestMapping(value = "/sendCode", method = RequestMethod.POST)
    public ResponseModel<CusMsg> sendCode(@RequestBody Map params) {
        String mobile = params.get("mobile").toString();
        return msgService.sendLoginCode(mobile);
    }
}
