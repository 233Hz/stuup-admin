package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.GrowGathererEnum;
import com.poho.stuup.constant.PeriodEnum;
import com.poho.stuup.dao.GrowUserMapper;
import com.poho.stuup.dao.GrowthItemMapper;
import com.poho.stuup.dao.GrowthMapper;
import com.poho.stuup.model.Growth;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.vo.GrowthRuleDescVO;
import com.poho.stuup.model.vo.UserApplyGrowthItemVO;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    @Resource
    private GrowthMapper growthMapper;

    @Override
    public List<UserApplyGrowthItemVO> getApplyGrowthItem(GrowGathererEnum type, Long userId) {
        List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getName));
        Map<Long, String> growthMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
        if (type == GrowGathererEnum.STUDENT) {
            return baseMapper.getStudentGrowthItems();
        } else if (type == GrowGathererEnum.TEACHER || type == GrowGathererEnum.STUDENT_UNION) {
            List<UserApplyGrowthItemVO> result = new ArrayList<>();
            List<GrowthItem> growthItems;
            if (Utils.isSuperAdmin(userId)) {
                growthItems = this.list(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getName, GrowthItem::getCode, GrowthItem::getFirstLevelId, GrowthItem::getSecondLevelId, GrowthItem::getThreeLevelId)
                        .eq(GrowthItem::getGatherer, type.getValue()));
            } else {
                List<Long> growIds = growUserMapper.findUserGrow(userId);
                if (growIds == null || growIds.isEmpty()) return result;
                growthItems = this.list(Wrappers.<GrowthItem>lambdaQuery()
                        .select(GrowthItem::getId, GrowthItem::getName, GrowthItem::getCode, GrowthItem::getFirstLevelId, GrowthItem::getSecondLevelId, GrowthItem::getThreeLevelId)
                        .eq(GrowthItem::getGatherer, type.getValue())
                        .in(GrowthItem::getId, growIds));
            }
            if (growthItems == null || growthItems.isEmpty()) return null;
            for (GrowthItem growthItem : growthItems) {
                UserApplyGrowthItemVO userApplyGrowthItemVO = new UserApplyGrowthItemVO();
                userApplyGrowthItemVO.setId(growthItem.getId());
                userApplyGrowthItemVO.setName(growthItem.getName());
                userApplyGrowthItemVO.setCode(growthItem.getCode());
                userApplyGrowthItemVO.setL1Id(growthItem.getFirstLevelId());
                userApplyGrowthItemVO.setL2Id(growthItem.getSecondLevelId());
                userApplyGrowthItemVO.setL3Id(growthItem.getThreeLevelId());
                userApplyGrowthItemVO.setL1Name(growthMap.get(growthItem.getFirstLevelId()));
                userApplyGrowthItemVO.setL2Name(growthMap.get(growthItem.getSecondLevelId()));
                userApplyGrowthItemVO.setL3Name(growthMap.get(growthItem.getThreeLevelId()));
                result.add(userApplyGrowthItemVO);
            }
            return result;
        } else {
            throw new RuntimeException("A type that does not exist");
        }
    }

    @Override
    public ResponseModel<Long> saveOrUpdateGrowthItem(GrowthItem data) {
        Long id = data.getId();
        if (PeriodEnum.UNLIMITED.getValue() != data.getScorePeriod()) {
            if (data.getScoreUpperLimit() == null) {
                return ResponseModel.failed("分值刷新周期选择除“不限”之外的类型必须填写每个周期内分值的上限");
            }
            if (data.getCollectLimit() != null) {
                return ResponseModel.failed("分值刷新周期选择除“不限”之外的类型无需填写可采集次数");
            }
        } else {
            if (data.getScoreUpperLimit() != null) {
                return ResponseModel.failed("分值刷新周期选择“不限”类型无需填写每个周期内分值的上限");
            }
        }
        if (id == null) {
            // 获取最大id
            Long maxId = baseMapper.fetchMaxId();
            data.setCode(StrUtil.format("CZ_{}", maxId + 1));
            return baseMapper.insert(data) > 0 ? ResponseModel.ok(data.getId(), "保存成功！") : ResponseModel.failed("保存失败！");
        } else {
            if (StrUtil.isNotBlank(data.getCode())) return ResponseModel.failed("项目编码不能修改");
            return baseMapper.updateById(data) > 0 ? ResponseModel.ok(data.getId(), "保存成功！") : ResponseModel.failed("保存失败！");
        }
    }

    @Override
    public List<GrowthRuleDescVO> getGrowthRuleDesc() {
        List<GrowthItem> growthItems = baseMapper.selectList(Wrappers.<GrowthItem>lambdaQuery()
                .select(GrowthItem::getId,
                        GrowthItem::getFirstLevelId,
                        GrowthItem::getSecondLevelId,
                        GrowthItem::getThreeLevelId,
                        GrowthItem::getName,
                        GrowthItem::getScorePeriod,
                        GrowthItem::getScoreUpperLimit,
                        GrowthItem::getCollectLimit,
                        GrowthItem::getScore,
                        GrowthItem::getCalculateType)
                .groupBy(GrowthItem::getFirstLevelId)
                .groupBy(GrowthItem::getSecondLevelId)
                .groupBy(GrowthItem::getThreeLevelId)
                .groupBy(GrowthItem::getId)
                .orderByAsc(GrowthItem::getFirstLevelId)
                .orderByAsc(GrowthItem::getSecondLevelId)
                .orderByAsc(GrowthItem::getThreeLevelId)
                .isNotNull(GrowthItem::getFirstLevelId));
        List<GrowthRuleDescVO> result = new ArrayList<>();
        if (growthItems != null && !growthItems.isEmpty()) {
            List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                    .select(Growth::getId, Growth::getName));
            Map<Long, String> growthIdForNameMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
            growths.clear();
            for (GrowthItem growthItem : growthItems) {
                GrowthRuleDescVO growthRuleDescVO = new GrowthRuleDescVO();
                growthRuleDescVO.setId(growthItem.getId());
                growthRuleDescVO.setName(growthItem.getName());
                growthRuleDescVO.setCycle(growthItem.getScorePeriod());
                growthRuleDescVO.setCycleUpperLimit(growthItem.getScoreUpperLimit());
                growthRuleDescVO.setScore(growthItem.getScore());
                growthRuleDescVO.setCalculateType(growthItem.getCalculateType());
                Optional.ofNullable(growthItem.getFirstLevelId())
                        .map(l1Id -> {
                            growthRuleDescVO.setL1Id(l1Id);
                            return growthIdForNameMap.get(l1Id);
                        })
                        .ifPresent(growthRuleDescVO::setL1);
                Optional.ofNullable(growthItem.getSecondLevelId())
                        .map(l2Id -> {
                            growthRuleDescVO.setL2Id(l2Id);
                            return growthIdForNameMap.get(l2Id);
                        })
                        .ifPresent(growthRuleDescVO::setL2);
                Optional.ofNullable(growthItem.getThreeLevelId())
                        .map(l3Id -> {
                            growthRuleDescVO.setL3Id(l3Id);
                            return growthIdForNameMap.get(l3Id);
                        })
                        .ifPresent(growthRuleDescVO::setL3);
                result.add(growthRuleDescVO);
            }
            growthIdForNameMap.clear();
        }
        return result;
    }

}
