package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Growth;
import com.poho.stuup.model.vo.GrowthTreeVO;
import com.poho.stuup.service.GrowthService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 成长项目表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@RestController
@RequestMapping("/growth")
public class GrowthController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private GrowthService growthService;


    @GetMapping("/tree")
    public ResponseModel<List<GrowthTreeVO>> tree() {
        return ResponseModel.ok(growthService.getGrowthTree());
    }

    @PostMapping("/saveOrUpdate")
    public ResponseModel<Long> saveOrUpdateGrowth(@Valid @RequestBody Growth growth) {
        String userId = ProjectUtil.obtainLoginUser(request);
        growth.setCreateUser(Long.parseLong(userId));
        if (growth.getId() != null && growth.getPid() != null && Objects.equals(growth.getId(), growth.getPid())) {
            return ResponseModel.failed("父节点不能为自己");
        }
        return growthService.saveOrUpdate(growth) ? ResponseModel.ok(growth.getId(), "添加成功！") : ResponseModel.failed("添加失败！");
    }

    @DeleteMapping("/del/{id}")
    public ResponseModel<Long> delGrowthById(@PathVariable("id") Long id) {
        return growthService.removeById(id) ? ResponseModel.ok(id, "删除成功！") : ResponseModel.failed("删除失败！");
    }


}
