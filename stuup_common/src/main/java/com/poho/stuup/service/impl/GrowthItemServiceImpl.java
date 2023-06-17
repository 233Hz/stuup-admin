package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.FloweringStageEnum;
import com.poho.stuup.dao.GrowUserMapper;
import com.poho.stuup.dao.GrowthItemMapper;
import com.poho.stuup.model.Config;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.FlowerVO;
import com.poho.stuup.model.vo.GrowthItemSelectVO;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 成长项 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@Service
public class GrowthItemServiceImpl extends ServiceImpl<GrowthItemMapper, GrowthItem> implements GrowthItemService {

    @Resource
    private IConfigService configService;

    @Resource
    private GrowUserMapper growUserMapper;

    @Override
    public Map<String, Long> getGrowthItemMap() {
        List<GrowthItem> itemList = baseMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getCode));
        return itemList.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));
    }

    @Override
    public boolean isExist(String code) {
        return baseMapper.exists(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, code));
    }

    @Override
    public boolean isExist(Long id, String code) {
        return baseMapper.exists(Wrappers.<GrowthItem>lambdaQuery()
                .ne(GrowthItem::getId, id)
                .eq(GrowthItem::getCode, code));
    }

    @Override
    public GrowthItem getGrowthItemByCode(String recCode) {
        return baseMapper.selectOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, recCode));
    }

    @Override
    public ResponseModel<List<GrowthItem>> getUserGrowthItems(Long userId) {
        if (Utils.isSuperAdmin(userId))
            return ResponseModel.ok(this.list(Wrappers.<GrowthItem>lambdaQuery().select(GrowthItem::getId, GrowthItem::getName, GrowthItem::getCode)));
        List<Long> growIds = growUserMapper.findUserGrow(userId);
        if (CollUtil.isEmpty(growIds)) return ResponseModel.failed("您没有可导入的项目");
        return ResponseModel.ok(this.list(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getName, GrowthItem::getCode)
                .in(GrowthItem::getId, growIds)));
    }

    @Override
    public boolean verifyRemainingFillNum(Long userId, String growCode) {
        //TODO
        return true;
    }

    @Override
    public FlowerVO getFlowerConfig() {
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

    @Override
    public List<GrowthItemSelectVO> getStudentGrowthItems() {
        return baseMapper.getStudentGrowthItems();
    }

}
