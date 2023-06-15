package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.api.config.MinioConfig;
import com.poho.stuup.constant.RecEnum;
import com.poho.stuup.handle.RecExcelHandle;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.service.GrowUserService;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.util.MinioUtils;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
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
    private MinioConfig prop;

    @Resource
    private GrowthItemService growthItemService;

    @Resource
    private GrowUserService growUserService;

    @PostMapping("/import")
    public ResponseModel<List<ExcelError>> recImport(MultipartFile file, @RequestParam(required = false) Map<String, Object> params) {
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
        RecExcelHandle handle = RecEnum.getHandle(recCode);
        return handle.recImport(file, growthItem, params);
    }

    /**
     * @description: 下载模板
     * @param: recCode
     * @param: response
     * @return: void
     * @author BUNGA
     * @date: 2023/6/14 17:07
     */
    @GetMapping("/downTemp")
    public void downTemp(@RequestParam("rec_code") String recCode, HttpServletResponse res) {
        RecEnum recEnum = RecEnum.getEnumByCode(recCode);
        if (recEnum == null) recCode = "CZ_DEFAULT";
        String growthItemName = growthItemService.getObj(Wrappers.<GrowthItem>lambdaQuery().select(GrowthItem::getName).eq(GrowthItem::getCode, recCode), Object::toString);
        String fileName, suffix = ".xlsx";
        if (StrUtil.isBlank(growthItemName)) {
            fileName = StrUtil.format("默认导入模板{}", suffix);
        } else {
            fileName = StrUtil.format("{}_导入模板{}", growthItemName, suffix);
        }
        MinioUtils.download(prop.getTempBucketName(), recCode + suffix, fileName, res);
    }
}
