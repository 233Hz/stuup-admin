package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecCaucusMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.service.RecCaucusService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private YearMapper yearMapper;
    @Resource
    private RecScoreService recScoreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecCaucusExcel(long batchCode, GrowthItem growthItem, List<RecCaucusExcel> excels, Map<String, Object> params) {
        Year year = yearMapper.findCurrYear();
        //=================保存数据=================
        List<RecCaucus> recCaucuses = excels.stream().map(excel -> {
            RecCaucus recCaucus = new RecCaucus();
            recCaucus.setYearId(year.getOid());
            recCaucus.setGrowId(growthItem.getId());
            recCaucus.setStudentId(excel.getStudentId());
            recCaucus.setName(excel.getName());
            recCaucus.setLevel(RecLevelEnum.getLabelValue(excel.getLevel()));
            recCaucus.setStartTime(DateUtil.parseDate(excel.getStartTime()));
            recCaucus.setEndTime(DateUtil.parseDate(excel.getEndTime()));
            recCaucus.setBatchCode(batchCode);
            return recCaucus;
        }).collect(Collectors.toList());
        // 计算学生成长积分
        List<Long> studentIds = recCaucuses.stream().map(RecCaucus::getStudentId).collect(Collectors.toList());
        recScoreService.calculateScore(studentIds, growthItem);
    }
}
