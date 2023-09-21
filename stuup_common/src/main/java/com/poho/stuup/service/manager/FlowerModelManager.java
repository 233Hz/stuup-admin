package com.poho.stuup.service.manager;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.poho.stuup.constant.CacheKeyConstant;
import com.poho.stuup.constant.FloweringStageEnum;
import com.poho.stuup.model.Config;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.service.IConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author BUNGA
 * @date 2023/9/12 13:21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlowerModelManager {

    private final IConfigService configService;

    @Cacheable(value = CacheKeyConstant.FLOWER_MODEL, key = "'getFlowerModel'")
    public FlowerVO queryFlowerModel() {
        log.debug("测试查询了数据库");
        FlowerVO flowerVO = new FlowerVO();
        for (FloweringStageEnum floweringStageEnum : FloweringStageEnum.values()) {
            Config config = configService.selectByPrimaryKey(floweringStageEnum.getConfigKey());
            try {
                int configValue = Integer.parseInt(config.getConfigValue());
                ReflectUtil.setFieldValue(flowerVO, floweringStageEnum.getFieldName(), configValue);
            } catch (Exception e) {
                throw new RuntimeException(StrUtil.format("系统配置：{}设置错误，请输入数字", floweringStageEnum.getDescription()));
            }
        }
        return flowerVO;
    }

}
