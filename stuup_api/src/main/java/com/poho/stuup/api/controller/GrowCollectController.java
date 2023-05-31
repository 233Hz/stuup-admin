package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.RecEnum;
import com.poho.stuup.handle.RecDefaultHandle;
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

    @PostMapping("/import")
    public ResponseModel recImport(MultipartFile file, @RequestParam(required = false) Map<String, Object> params) {
        String userId = ProjectUtil.obtainLoginUser(request);
        String code = (String) params.get("rec_code");
        // 校验是否可以导入
        boolean canImport = growthItemService.verifyRemainingFillNum(Long.valueOf(userId), code);
        if (!canImport) return ResponseModel.failed("导入次数已使用完");
        params.put("userId", userId);
        RecEnum recEnum = RecEnum.getByCode(code);
        if (recEnum == null) {
            return new RecDefaultHandle().recImport(file, params);
        }
        return recEnum.getHandle().recImport(file, params);
    }
}
