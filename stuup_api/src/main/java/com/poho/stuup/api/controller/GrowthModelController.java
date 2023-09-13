package com.poho.stuup.api.controller;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.FloweringStageEnum;
import com.poho.stuup.model.dto.FlowerDTO;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.service.FlowerModelService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author BUNGA
 * @description: 成长模型
 * @date 2023/6/7 10:20
 */
@RestController
@RequestMapping("/growthModel")
public class GrowthModelController {

    @Resource
    private FlowerModelService flowerModelService;

    @GetMapping
    public ResponseModel<FlowerVO> getFlowerModel() {
        return ResponseModel.ok(flowerModelService.getFlowerModel());
    }

    @PostMapping
    public ResponseModel<FlowerVO> setFlowerModel(@Valid @RequestBody FlowerDTO flowerDTO) {
        FloweringStageEnum floweringStageEnum = FloweringStageEnum.getByFieldName(flowerDTO.getKey());
        if (floweringStageEnum == null) {
            return ResponseModel.failed("设置的成长阶段不存在");
        }
        return ResponseModel.ok(flowerModelService.setFlowerModel(flowerDTO));
    }

}
