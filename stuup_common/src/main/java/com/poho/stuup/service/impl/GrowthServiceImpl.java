package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.GrowthMapper;
import com.poho.stuup.model.Growth;
import com.poho.stuup.model.vo.GrowthTreeVO;
import com.poho.stuup.service.GrowthService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 成长项目表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-24
 */
@Service
public class GrowthServiceImpl extends ServiceImpl<GrowthMapper, Growth> implements GrowthService {

    @Override
    public List<GrowthTreeVO> getGrowthTree() {
        List<Growth> growthList = this.list(Wrappers.<Growth>lambdaQuery()
                .select(Growth::getId, Growth::getPid, Growth::getName, Growth::getDescription, Growth::getSort)
                .orderByAsc(Growth::getSort));

        Map<Long, GrowthTreeVO> map = new HashMap<>();
        for (Growth growth : growthList) {
            GrowthTreeVO growthTreeVO = new GrowthTreeVO();
            growthTreeVO.setId(growth.getId());
            growthTreeVO.setPid(growth.getPid());
            growthTreeVO.setName(growth.getName());
            growthTreeVO.setDescription(growth.getDescription());
            growthTreeVO.setSort(growth.getSort());
            map.put(growth.getId(), growthTreeVO);
        }
        List<GrowthTreeVO> growthTreeList = new ArrayList<>();
        for (Growth growth : growthList) {
            GrowthTreeVO growthTree = map.get(growth.getId());
            GrowthTreeVO parentGrowthTree = map.get(growth.getPid());
            if (parentGrowthTree != null) {
                List<GrowthTreeVO> children = parentGrowthTree.getChildren();
                if (CollUtil.isEmpty(children)) {
                    List<GrowthTreeVO> treeChildren = new ArrayList<>();
                    treeChildren.add(growthTree);
                    parentGrowthTree.setChildren(treeChildren);
                } else {
                    children.add(growthTree);
                }
            } else {
                growthTreeList.add(growthTree);
            }
        }
        return growthTreeList;
    }
}
