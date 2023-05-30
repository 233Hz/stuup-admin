package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.GrowUserMapper;
import com.poho.stuup.dao.GrowthItemMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.service.GrowthItemService;
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
    private GrowUserMapper growUserMapper;

    @Override
    public Map<String, Long> getGrowthItemMap() {
        List<GrowthItem> itemList = baseMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getCode));
        return itemList.stream().collect(Collectors.toMap(GrowthItem::getCode, GrowthItem::getId));
    }

    @Override
    public boolean isExist(String name, String code) {
        return baseMapper.exists(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getName, name)
                .or()
                .eq(GrowthItem::getCode, code));
    }

    @Override
    public boolean isExist(Long id, String name, String code) {
        return baseMapper.exists(Wrappers.<GrowthItem>lambdaQuery()
                .ne(GrowthItem::getId, id)
                .and(queryWrapper -> queryWrapper.eq(GrowthItem::getName, name).or().eq(GrowthItem::getCode, code)));
    }

    @Override
    public GrowthItem getGrowthItemByCode(String recCode) {
        return baseMapper.selectOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, recCode));
    }

    @Override
    public List<GrowthItem> getUserGrowthItems(Long userId) {
        List<Long> growIds = growUserMapper.findUserGrow(userId);
        return this.list(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId, GrowthItem::getName, GrowthItem::getScore)
                .in(!Utils.isSuperAdmin(userId), GrowthItem::getId, growIds));
    }

}
