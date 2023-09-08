package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.GardenTypeEnum;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.GrowGardenDTO;
import com.poho.stuup.model.vo.GrowGardenVO;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/garden")
public class GardenController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private StuScoreService stuScoreService;

    @Resource
    private IUserService userService;

    @GetMapping("/page")
    private ResponseModel<IPage<GrowGardenVO>> pageGrowGarden(Page<GrowGardenVO> page, GrowGardenDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        Integer gardenType = query.getGardenType();
        if (gardenType == null) return ResponseModel.failed("请确认要查询的花园类型");
        if (gardenType != GardenTypeEnum.XCJ.getValue() &&
                gardenType != GardenTypeEnum.BMH.getValue() &&
                gardenType != GardenTypeEnum.XHH.getValue())
            return ResponseModel.failed("查询的花园类型不存在");
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null) return ResponseModel.failed("未查询到您的用户信息");
        Integer userType = user.getUserType();
        if (userType == null) return ResponseModel.failed("您的账号未设置用户信息无法查询");
        query.setUser(user);
        return ResponseModel.ok(stuScoreService.pageGrowGarden(page, query));
    }


}
