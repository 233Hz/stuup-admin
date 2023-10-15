package com.poho.stuup.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckSafe;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.GrowGathererEnum;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.GrowthRuleDescVO;
import com.poho.stuup.model.vo.UserApplyGrowthItemVO;
import com.poho.stuup.service.GrowthItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    private GrowthItemService growthItemService;

    @GetMapping("/page")
    public ResponseModel<Page<GrowthItem>> getPage(Page<GrowthItem> page, GrowthItem growthItem) {
        return ResponseModel.ok(growthItemService.page(page, Wrappers.<GrowthItem>lambdaQuery()
                .ne(GrowthItem::getFirstLevelId, 0)
                .eq(growthItem.getFirstLevelId() != null, GrowthItem::getFirstLevelId, growthItem.getFirstLevelId())
                .eq(growthItem.getSecondLevelId() != null, GrowthItem::getSecondLevelId, growthItem.getSecondLevelId())
                .eq(growthItem.getThreeLevelId() != null, GrowthItem::getThreeLevelId, growthItem.getThreeLevelId())
                .like(StrUtil.isNotEmpty(growthItem.getName()), GrowthItem::getName, growthItem.getName())
                .eq(growthItem.getCalculateType() != null, GrowthItem::getCalculateType, growthItem.getCalculateType())));
    }

    @SaCheckPermission("growth_item_add_edit")
    @PostMapping("/saveOrUpdate")
    public ResponseModel<Long> saveOrUpdateGrowthItem(@Valid @RequestBody GrowthItem data) {
        String userId = StpUtil.getLoginId().toString();
        data.setCreateUser(Long.parseLong(userId));
        return growthItemService.saveOrUpdateGrowthItem(data);
    }

    @SaCheckSafe("growth_item_del")
    @SaCheckPermission("growth_item_del")
    @DeleteMapping("/del/{id}")
    public ResponseModel<Boolean> delGrowthItemById(@PathVariable("id") Long id) {
        return growthItemService.removeById(id) ? ResponseModel.ok(true, "删除成功！") : ResponseModel.failed("删除失败！");
    }

    @GetMapping("/self/apply")
    public ResponseModel<List<UserApplyGrowthItemVO>> getSelfApplyItem(@RequestParam("type") String type) {
        GrowGathererEnum gathererEnum = GrowGathererEnum.getEnumByCode(type);
        if (gathererEnum == null) return ResponseModel.failed("类型不存在");
        String userId = StpUtil.getLoginId().toString();
        return ResponseModel.ok(growthItemService.getApplyGrowthItem(gathererEnum, Long.valueOf(userId)));
    }

    @GetMapping("/rule/desc")
    public ResponseModel<List<GrowthRuleDescVO>> getGrowthRuleDesc() {
        return ResponseModel.ok(growthItemService.getGrowthRuleDesc());
    }
}
