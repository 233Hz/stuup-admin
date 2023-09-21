package com.poho.stuup.api.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.GrowGathererEnum;
import com.poho.stuup.model.GrowUser;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.dto.GrowthItemLeaderDTO;
import com.poho.stuup.model.dto.GrowthItemUserDTO;
import com.poho.stuup.model.vo.GrowthItemUserVO;
import com.poho.stuup.service.GrowUserService;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.service.IUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 项目负责人表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/growUser")
public class GrowUserController {

    @Resource
    private IUserService userService;

    @Resource
    private GrowUserService growUserService;

    @Resource
    private GrowthItemService growthItemService;

    /**
     * @description: 设置项目负责人
     * @param:
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/14 18:54
     */
    @SaCheckPermission("growth_item_admin")
    @PostMapping("/setGrowthItemUser")
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> setGrowthItemUser(@Valid @RequestBody GrowthItemLeaderDTO data) {
        Long growId = data.getGrowthItemId();
        GrowthItem growthItem = growthItemService.getById(growId);
        Integer growType = growthItem.getType();
        if (growType != GrowGathererEnum.TEACHER.getValue() && growType != GrowGathererEnum.STUDENT_UNION.getValue())
            return ResponseModel.failed("该项目类型无指定负责人");
        growUserService.remove(Wrappers.<GrowUser>lambdaQuery()
                .eq(GrowUser::getGrowId, data.getGrowthItemId()));
        List<Long> userIds = data.getUserIds();
        List<GrowUser> growUsers = userIds.stream().map(userId -> {
            GrowUser growUser = new GrowUser();
            growUser.setGrowId(growId);
            growUser.setUserId(userId);
            return growUser;
        }).collect(Collectors.toList());
        growthItemService.update(Wrappers.<GrowthItem>lambdaUpdate()
                .set(GrowthItem::getGatherer, data.getAssignType())
                .eq(GrowthItem::getId, growId));
        growUserService.saveBatch(growUsers);
        return ResponseModel.ok(null, "设置成功");
    }

    /**
     * @description: 查询项目负责人
     * @param: growthItemId
     * @return: com.poho.common.custom.ResponseModel<java.util.List < com.poho.stuup.model.vo.GrowthItemUserVO>>
     * @author BUNGA
     * @date: 2023/6/15 14:33
     */
    @GetMapping("/getGrowItemUser/{growthItemId}")
    public ResponseModel<List<GrowthItemUserVO>> getGrowItemUser(@PathVariable("growthItemId") Long growthItemId) {
        return ResponseModel.ok(growUserService.getGrowItemUser(growthItemId));
    }

    @GetMapping("/page/user")
    public ResponseModel<IPage<GrowthItemUserVO>> paginateGrowthItemUser(Page<GrowthItemUserVO> page, GrowthItemUserDTO query) {
        return ResponseModel.ok(userService.paginateGrowthItemUser(page, query));
    }

}
