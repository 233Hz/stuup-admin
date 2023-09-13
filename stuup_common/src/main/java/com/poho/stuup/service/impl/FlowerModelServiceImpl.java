package com.poho.stuup.service.impl;

import cn.hutool.core.util.ReflectUtil;
import com.poho.stuup.constant.RedisKeyConstant;
import com.poho.stuup.model.dto.FlowerDTO;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.service.FlowerModelService;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.manager.FlowerModelManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

/**
 * @author BUNGA
 * @description TODO
 * @date 2023/9/12 10:54
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowerModelServiceImpl implements FlowerModelService {

    private final IConfigService configService;
    private final FlowerModelManager flowerModelManager;

    @Override
    public FlowerVO getFlowerModel() {
        return flowerModelManager.queryFlowerModel();
    }

    @CachePut(value = RedisKeyConstant.FLOWER_MODEL, key = "'getFlowerModel'")
    @Override
    public FlowerVO setFlowerModel(FlowerDTO flowerDTO) {
        configService.saveOrUpdate(flowerDTO.getKey(), flowerDTO.getValue());
        FlowerVO flowerVO = flowerModelManager.queryFlowerModel();
        ReflectUtil.setFieldValue(flowerVO, flowerDTO.getKey(), Integer.parseInt(flowerDTO.getValue()));
        return flowerVO;
    }
}
