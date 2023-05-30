package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.GrowthItemMapper;
import com.poho.stuup.dao.RecCaucusMapper;
import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.service.RecCaucusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 参加党团学习项目记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecCaucusServiceImpl extends ServiceImpl<RecCaucusMapper, RecCaucus> implements RecCaucusService {

    @Resource
    private GrowthItemMapper growthItemMapper;

    @Override
    public boolean saveData(RecCaucusExcel data) {
        //=================转换数据=================
        RecCaucus recCaucus = new RecCaucus();
        recCaucus.setStudentId(data.getStudentId());
        recCaucus.setName(data.getName());
        recCaucus.setLevel(RecLevelEnum.getLabelValue(data.getLevel()));
        recCaucus.setStartTime(DateUtil.parseDate(data.getStartTime()));
        recCaucus.setEndTime(DateUtil.parseDate(data.getEndTime()));
        recCaucus.setRole(RecRoleEnum.getRoleValue(data.getRole()));
        recCaucus.setBatchCode(data.getBatchCode());
        //=================计算积分=================

        //=================插入数据=================
        return baseMapper.insert(recCaucus) > 0;
    }
}
