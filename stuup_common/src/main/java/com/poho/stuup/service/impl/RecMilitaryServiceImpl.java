package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.MilitaryLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecMilitaryMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.dto.RecMilitaryDTO;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.model.vo.RecMilitaryVO;
import com.poho.stuup.service.RecMilitaryService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 军事训练记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecMilitaryServiceImpl extends ServiceImpl<RecMilitaryMapper, RecMilitary> implements RecMilitaryService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private RecScoreService recScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecMilitaryExcel(long batchCode, GrowthItem growthItem, List<RecMilitaryExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Year currYear = yearMapper.findCurrYear();
        //=================保存数据=================
        List<RecMilitary> recMilitaries = excels.stream().map(excel -> {
            RecMilitary recMilitary = new RecMilitary();
            recMilitary.setYearId(currYear.getOid());
            recMilitary.setGrowId(growthItem.getId());
            recMilitary.setStudentId(excel.getStudentId());
            recMilitary.setLevel(MilitaryLevelEnum.getLabelValue(excel.getLevel()));
            recMilitary.setExcellent(WhetherEnum.getLabelValue(excel.getExcellent()));
            recMilitary.setBatchCode(batchCode);
            return recMilitary;
        }).collect(Collectors.toList());
        this.saveBatch(recMilitaries);
        // 插入一条导入日志
        RecLog recLog = new RecLog();
        recLog.setGrowId(growthItem.getId());
        recLog.setYearId(currYear.getOid());
        recLog.setCreateUser(Long.valueOf(userId));
        recLog.setBatchCode(batchCode);
        recLogMapper.insert(recLog);
        // 计算学生成长积分
        List<Long> studentIds = recMilitaries.stream().map(RecMilitary::getStudentId).collect(Collectors.toList());
        recScoreService.calculateScore(studentIds, currYear.getOid(), growthItem);
    }

    @Override
    public IPage<RecMilitaryVO> getRecMilitaryPage(Page<RecMilitaryVO> page, RecMilitaryDTO query) {
        return baseMapper.getRecMilitaryPage(page, query);
    }
}
