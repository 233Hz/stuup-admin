package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.GrowGathererEnum;
import com.poho.stuup.model.GrowUser;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.dto.GrowthItemUserDTO;
import com.poho.stuup.model.vo.SimpleUserVO;
import com.poho.stuup.service.GrowUserService;
import com.poho.stuup.service.GrowthItemService;
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
    @PostMapping("/setGrowthItemUser")
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> setGrowthItemUser(@Valid @RequestBody GrowthItemUserDTO data) {
        Long growId = data.getGrowId();
        GrowthItem growthItem = growthItemService.getById(growId);
        Integer growType = growthItem.getType();
        if (growType == GrowGathererEnum.STUDENT.getValue())
            return ResponseModel.failed("学生申请的项目无法设置指定负责人");
        growUserService.remove(Wrappers.<GrowUser>lambdaQuery()
                .eq(GrowUser::getGrowId, data.getGrowId()));
        List<Long> userIds = data.getUserIds();
        List<GrowUser> growUsers = userIds.stream().map(userId -> {
            GrowUser growUser = new GrowUser();
            growUser.setGrowId(growId);
            growUser.setUserId(userId);
            return growUser;
        }).collect(Collectors.toList());
        boolean save = growUserService.saveBatch(growUsers);
        return save ? ResponseModel.ok(null, "设置成功") : ResponseModel.failed("设置失败");
    }

    /**
     * @description: 查询项目负责人
     * @param: growId
     * @return: com.poho.common.custom.ResponseModel<java.util.List < com.poho.stuup.model.vo.SimpleUserVO>>
     * @author BUNGA
     * @date: 2023/6/15 14:33
     */
    @GetMapping("/getGrowItemUser/{growId}")
    public ResponseModel<List<SimpleUserVO>> getGrowItemUser(@PathVariable("growId") Long growId) {
        return ResponseModel.ok(growUserService.getGrowItemUser(growId));
    }

}
