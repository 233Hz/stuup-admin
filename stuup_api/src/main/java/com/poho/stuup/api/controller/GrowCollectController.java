package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.RecEnum;
import com.poho.stuup.handle.RecDefaultHandle;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.service.GrowUserService;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 成长项目数据采集
 * @date 2023/5/26 9:58
 */
@RestController
@RequestMapping("/grow")
public class GrowCollectController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GrowthItemService growthItemService;

    @Resource
    private GrowUserService growUserService;

    @PostMapping("/import")
    public ResponseModel recImport(MultipartFile file, @RequestParam(required = false) Map<String, Object> params) {
        String userId = ProjectUtil.obtainLoginUser(request);
        String recCode = (String) params.get("rec_code");
        GrowthItem growthItem = growthItemService.getOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, recCode));
        if (growthItem == null) return ResponseModel.failed("导入项目不存在");
        Long growId = growthItem.getId();
        // 查询项目负责人
        boolean isGrowUser = growUserService.isGrowUser(Long.parseLong(userId), growId);
        if (!isGrowUser) return ResponseModel.failed("不是该项目负责人，无法导入");
        // 校验是否可以导入
        boolean canImport = growthItemService.verifyRemainingFillNum(Long.valueOf(userId), recCode);
        if (!canImport) return ResponseModel.failed("导入次数已使用完");
        params.put("userId", userId);
        RecEnum recEnum = RecEnum.getByCode(recCode);
        if (recEnum == null) {
            return new RecDefaultHandle().recImport(file, growthItem, params);
        }
        return recEnum.getHandle().recImport(file, growthItem, params);
    }
}
