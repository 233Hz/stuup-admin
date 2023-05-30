package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 成长项 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/growthItem")
public class GrowthItemController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GrowthItemService growthItemService;

    @GetMapping("/page")
    public ResponseModel<Page<GrowthItem>> getPage(Page<GrowthItem> page, GrowthItem growthItem) {
        return ResponseModel.ok(growthItemService.page(page, Wrappers.<GrowthItem>lambdaQuery()
                .eq(growthItem.getGrowthId() != null, GrowthItem::getGrowthId, growthItem.getGrowthId())
                .like(StrUtil.isNotEmpty(growthItem.getName()), GrowthItem::getName, growthItem.getName())
                .eq(growthItem.getCalculateType() != null, GrowthItem::getCalculateType, growthItem.getCalculateType())));
    }

    @PostMapping("/saveOrUpdate")
    public ResponseModel<Long> saveOrUpdateGrowthItem(@Valid @RequestBody GrowthItem data) {
        Long id = data.getId();
        String name = data.getName();
        String code = data.getCode();
        if (id == null) {
            boolean exist = growthItemService.isExist(name, code);
            if (exist) return ResponseModel.failed("该成长项名称或者编号已存在，请修改后重试！");
            String userId = ProjectUtil.obtainLoginUser(request);
            data.setCreateUser(Long.parseLong(userId));
        } else {
            boolean exist = growthItemService.isExist(id, name, data.getCode());
            if (exist) return ResponseModel.failed("该成长项名称或者编号已存在，请修改后重试！");
        }
        if (PeriodEnum.UNLIMITED.getValue() != data.getFillPeriod()) {
            if (data.getFillPeriodNum() == null) {
                return ResponseModel.failed("项目录入周期选择除”不限“之外的类型必须填写项目周期内可录入次数");
            }
        } else {
            if (data.getFillPeriodNum() != null) {
                return ResponseModel.failed("项目录入周期选择”不限“类型无需填写项目周期内可录入次数");
            }
        }
        if (PeriodEnum.UNLIMITED.getValue() != data.getScorePeriod()) {
            if (data.getScoreUpperLimit() == null) {
                return ResponseModel.failed("分值刷新周期选择除“不限”之外的类型必须填写每个周期内分值的上限");
            }
        } else {
            if (data.getScoreUpperLimit() != null) {
                return ResponseModel.failed("分值刷新周期选择“不限”类型无需填写每个周期内分值的上限");
            }
        }
        return growthItemService.saveOrUpdate(data) ? ResponseModel.ok(data.getId(), "保存成功！") : ResponseModel.failed("保存失败！");
    }

    @DeleteMapping("/del/{id}")
    public ResponseModel delGrowthItemById(@PathVariable("id") Long id) {
        return growthItemService.removeById(id) ? ResponseModel.ok("删除成功！") : ResponseModel.failed("删除失败！");
    }

    @GetMapping("/myGrowthItems")
    public ResponseModel<List<GrowthItem>> getUserGrowthItems() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return ResponseModel.ok(growthItemService.getUserGrowthItems(Long.valueOf(userId)));
    }

}