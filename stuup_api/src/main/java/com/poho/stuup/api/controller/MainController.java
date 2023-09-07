package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.custom.CusUser;
import com.poho.stuup.custom.CusUserLogin;
import com.poho.stuup.service.IUserService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Author: wupeng
 * @Description:
 * @Date: Created in 17:18 2020/9/3
 * @Modified By:
 */
@Api(tags = "登录相关接口")
@RestController
@RequestMapping("/")
public class MainController {
    @Resource
    private IUserService userService;

    /**
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseModel<CusUser> login(@RequestBody CusUserLogin user) {
        return userService.checkLogin(user.getLoginName(), user.getPassword());
    }
}
