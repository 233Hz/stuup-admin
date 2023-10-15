package com.poho.stuup.api.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.api.config.MinioConfig;
import com.poho.stuup.constant.GrowGathererEnum;
import com.poho.stuup.constant.RecEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.growth.GrowthUtils;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.growth.RecImportResult;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.User;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.service.*;
import com.poho.stuup.util.MinioUtils;
import com.poho.stuup.util.Utils;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
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
    private MinioConfig prop;

    @Resource
    private GrowthItemService growthItemService;

    @Resource
    private GrowUserService growUserService;

    @Resource
    private IUserService userService;

    @Resource
    private IYearService yearService;

    @Resource
    private SemesterService semesterService;

    @PostMapping("/import")
    public ResponseModel<List<ExcelError>> importGrowth(MultipartFile file, @RequestParam String recCode) {
        Long userId = Long.valueOf(StpUtil.getLoginId().toString());
        GrowthItem growthItem = growthItemService.getOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, recCode));
        if (growthItem == null) return ResponseModel.failed("导入项目不存在");
        Integer gatherer = growthItem.getGatherer();
        if (gatherer != GrowGathererEnum.TEACHER.getValue())
            return ResponseModel.failed("当前项目采集类型无法通过该入口导入");
        Long growId = growthItem.getId();
        // 查询项目负责人
        boolean isGrowUser = growUserService.isGrowthItemUser(userId, growId);
        if (!isGrowUser && !Utils.isSuperAdmin(userId))
            return ResponseModel.failed("不是该项目负责人，无法导入");
        Long yearId = yearService.getCurrentYearId();
        if (yearId == null) return ResponseModel.failed("不在当前学年时间段内");
        Long semesterId = semesterService.getCurrentSemesterId();
        if (semesterId == null) return ResponseModel.failed("不在当前学期时间段内");
        RecImportParams params = RecImportParams
                .builder()
                .userId(userId)
                .yearId(yearId)
                .semesterId(semesterId)
                .growthItem(growthItem)
                .build();
        RecImportResult recImportResult = GrowthUtils.recImport(growthItem.getHandle(), file, params);
        int total = recImportResult.getTotal();
        int success = recImportResult.getSuccess();
        int fail = recImportResult.getFail();
        if (total == 0) return ResponseModel.failed("Excel为空！");
        List<ExcelError> errors = recImportResult.getErrors();
        if (errors != null && !errors.isEmpty()) {
            return ResponseModel.ok(errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", total, success, fail));
        }
        return ResponseModel.ok(null, "导入成功");
    }


    @SneakyThrows
    @GetMapping("/export")
    public void recExport(HttpServletResponse response, @RequestParam(required = false) Map<String, Object> params) {
        String userId = StpUtil.getLoginId().toString();
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        if (user == null || user.getUserType() != UserTypeEnum.TEACHER.getValue())
            throw new RuntimeException("无权限导出");
        String recCode = (String) params.get("rec_code");
        if (recCode == null) throw new RuntimeException("请选择要导出的项目");
        GrowthItem growthItem = growthItemService.getOne(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getName, GrowthItem::getHandle)
                .eq(GrowthItem::getCode, recCode));
        if (growthItem == null) throw new RuntimeException("要导出的项目不存在");
        String fileName = URLEncoder.encode(growthItem.getName(), "UTF-8");
        response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
        response.setHeader("Content-disposition", "attachment;fileName=" + fileName + ".xlsx");
        if (params.get("yearId") == null) throw new RuntimeException("请选择要导出的学年");
        GrowthUtils.recExport(response, growthItem.getHandle(), params);
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
    public void downTemp(@RequestParam("recCode") String recCode, HttpServletResponse res) {
        RecEnum recEnum = RecEnum.getEnumByCode(recCode);
        String fileName, suffix = ".xlsx";
        if (recEnum == null) {
            fileName = StrUtil.format("默认导入模板{}", suffix);
        } else {
            fileName = StrUtil.format("{}{}", recEnum.getTempName(), suffix);
        }
        MinioUtils.download(prop.getTempBucketName(), fileName, fileName, res);
    }
}
