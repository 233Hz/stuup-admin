package com.poho.stuup.api.controller;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.FloweringStageEnum;
import com.poho.stuup.model.Config;
import com.poho.stuup.model.dto.FlowerDTO;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.service.IConfigService;
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
    private IConfigService configService;

    @GetMapping("/flowers")
    public ResponseModel<FlowerVO> getFlowerConfig() {
        FlowerVO flowerVO = new FlowerVO();
        for (FloweringStageEnum floweringStageEnum : FloweringStageEnum.values()) {
            Config config = configService.selectByPrimaryKey(floweringStageEnum.getConfigKey());
            try {
                int configValue = Integer.parseInt(config.getConfigValue());
                ReflectUtil.setFieldValue(flowerVO, floweringStageEnum.getFieldName(), configValue);
            } catch (Exception e) {
                return ResponseModel.failed(StrUtil.format("系统配置：{}设置错误，请输入数字", floweringStageEnum.getDescription()));
            }
        }
        return ResponseModel.ok(flowerVO);
    }

    @PostMapping("/setFlowerConfig")
    public ResponseModel setFlowerConfig(@Valid @RequestBody FlowerDTO flowerDTO) {
        FloweringStageEnum floweringStageEnum = FloweringStageEnum.getByFieldName(flowerDTO.getKey());
        if (floweringStageEnum == null) {
            return ResponseModel.failed("设置的成长阶段不存在");
        }
        String configKey = floweringStageEnum.getConfigKey();
        return ResponseModel.ok(configService.saveOrUpdate(configKey, flowerDTO.getValue()));
    }

}
