package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
    public ResponseModel<Long> saveOrUpdateGrowthItem(@Valid @RequestBody GrowthItem growthItem) {
        if (growthItem.getId() == null) {
            String userId = ProjectUtil.obtainLoginUser(request);
            growthItem.setCreateUser(Long.parseLong(userId));
        }
        return growthItemService.saveOrUpdate(growthItem) ? ResponseModel.ok(growthItem.getId(), "添加成功！") : ResponseModel.failed("添加失败！");
    }

    @DeleteMapping("/del/{id}")
    public ResponseModel delGrowthItemById(@PathVariable("id") Long id) {
        return growthItemService.removeById(id) ? ResponseModel.ok("删除成功！") : ResponseModel.failed("删除失败！");
    }

}
