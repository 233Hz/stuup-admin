package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.GrowthItemSelectVO;
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
                .ne(GrowthItem::getFirstLevelId, 0)
                .eq(growthItem.getFirstLevelId() != null, GrowthItem::getFirstLevelId, growthItem.getFirstLevelId())
                .eq(growthItem.getSecondLevelId() != null, GrowthItem::getSecondLevelId, growthItem.getSecondLevelId())
                .eq(growthItem.getThreeLevelId() != null, GrowthItem::getThreeLevelId, growthItem.getThreeLevelId())
                .like(StrUtil.isNotEmpty(growthItem.getName()), GrowthItem::getName, growthItem.getName())
                .eq(growthItem.getCalculateType() != null, GrowthItem::getCalculateType, growthItem.getCalculateType())));
    }

    @PostMapping("/saveOrUpdate")
    public ResponseModel<Long> saveOrUpdateGrowthItem(@Valid @RequestBody GrowthItem data) {
        String userId = ProjectUtil.obtainLoginUser(request);
        data.setCreateUser(Long.parseLong(userId));
        return growthItemService.saveOrUpdateGrowthItem(data);
    }

    @DeleteMapping("/del/{id}")
    public ResponseModel<Boolean> delGrowthItemById(@PathVariable("id") Long id) {
        return growthItemService.removeById(id) ? ResponseModel.ok(true, "删除成功！") : ResponseModel.failed("删除失败！");
    }

    @GetMapping("/self/apply")
    public ResponseModel<List<GrowthItem>> getSelfApplyItem(@RequestParam("type") String type) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return growthItemService.getSelfApplyItem(type, Long.valueOf(userId));
    }


    @GetMapping("/studentGrowthItems")
    public ResponseModel<List<GrowthItemSelectVO>> getStudentGrowthItems() {
        return ResponseModel.ok(growthItemService.getStudentGrowthItems());
    }
}
