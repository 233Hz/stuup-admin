package com.poho.stuup.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.common.custom.ResponseModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/saToken/")
public class SaTokenController {

    // 测试登录  ---- http://localhost:9000/stuup_api/saToken/testLogin?name=zhang&pwd=123456
    @SaIgnore
    @RequestMapping("testLogin")
    public ResponseModel<String> testLogin(String name, String pwd) {
        // 此处仅作模拟示例，真实项目需要从数据库中查询数据进行比对
        if ("zhang".equals(name) && "123456".equals(pwd)) {
            StpUtil.login(10001);
            return ResponseModel.ok("登录成功");
        }
        return ResponseModel.failed("登录失败");
    }

    // 测试权限校验：必须具有指定权限才能进入该方法  http://localhost:9000/stuup_api/saToken/testPermission
    @SaCheckPermission("testPermission")
    @RequestMapping("testPermission")
    public String testPermission() {
        return "测试权限";
    }

    // 测试二级认证校验：必须二级认证之后才能进入该方法 http://localhost:9000/stuup_api/saToken/testSafe
    @SaCheckSafe()
    @RequestMapping("testSafe")
    public String testSafe() {
        return "测试二级认证";
    }


    // 查询登录状态  ---- http://localhost:9000/stuup_api/saToken/isLogin
    @RequestMapping("isLogin")
    public ResponseModel<String> isLogin() {
        return ResponseModel.ok("是否登录：" + StpUtil.isLogin());
    }

    // 查询 Token 信息  ---- http://localhost:9000/stuup_api/saToken/tokenInfo
    @RequestMapping("tokenInfo")
    public ResponseModel<SaTokenInfo> tokenInfo() {
        return ResponseModel.ok(StpUtil.getTokenInfo());
    }


    // 提供密码进行二级认证 http://localhost:9000/stuup_api/saToken/openSafe?password=013579
    @RequestMapping("openSafe")
    public ResponseModel<String> openSafe(String code, String password) {
        // 比对密码
        if ("013579".equals(password)) {
            // 比对成功，为当前会话打开二级认证，有效期为120秒
            if (StrUtil.isNotBlank(code)) {
                StpUtil.openSafe(code, 120);
            } else {
                StpUtil.openSafe(120);
            }
            return ResponseModel.ok("二级认证成功");
        }
        // 如果密码校验失败，则二级认证也会失败
        return ResponseModel.failed("二级认证失败");
    }

    // 注销  ---- http://localhost:9000/stuup_api/saToken/logout
    @SaIgnore
    @RequestMapping("logout")
    public ResponseModel<String> logout(Long userId) {
        StpUtil.logout(userId);
        return ResponseModel.ok("注销成功");
    }

}
