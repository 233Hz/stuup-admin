package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.MilitaryLevelEnum;
import com.poho.stuup.constant.RecEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.GrowthItemMapper;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecMilitaryMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.model.dto.RecMilitaryDTO;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.model.vo.RecMilitaryVO;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecMilitaryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Resource
    private GrowthItemMapper growthItemMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecMilitaryExcel(List<RecMilitaryExcel> excels, RecImportParams params) {
        long batchCode = System.currentTimeMillis();
        Long userId = params.getUserId();
        Long yearId = params.getYearId();
        Long semesterId = params.getSemesterId();
        GrowthItem growthItem = params.getGrowthItem();
        GrowthItem growthItem1 = growthItemMapper.selectOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, RecEnum.REC_MILITARY_EXCELLENT.getCode()));
        GrowthItem growthItem2 = growthItemMapper.selectOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, RecEnum.REC_MILITARY_QUALIFIED.getCode()));
        List<Long> studentIds1 = new ArrayList<>();
        List<Long> studentIds2 = new ArrayList<>();
        for (RecMilitaryExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            // 过滤出优秀学员
            Integer excellent = WhetherEnum.getValueForLabel(excel.getExcellent());
            if (excellent != null && WhetherEnum.YES.getValue() == excellent) {
                recDefault.setGrowId(growthItem1.getId());
                recDefaultMapper.insert(recDefault);
                studentIds1.add(excel.getStudentId());
            }
            //过滤出合格学员
            Integer level = MilitaryLevelEnum.getValueForLabel(excel.getLevel());
            if (level != null && MilitaryLevelEnum.QUALIFIED.getValue() == level) {
                recDefault.setGrowId(growthItem2.getId());
                recDefaultMapper.insert(recDefault);
                studentIds1.add(excel.getStudentId());
            }
        }
        RecLog recLog = new RecLog();
        recLog.setYearId(yearId);
        recLog.setSemesterId(semesterId);
        recLog.setGrowId(growthItem.getId());
        recLog.setCreateUser(userId);
        recLog.setBatchCode(batchCode);
        recLogMapper.insert(recLog);
        recAddScoreService.batchCalculateScore(studentIds1, growthItem1, yearId, semesterId, DateUtil.date(batchCode));
        recAddScoreService.batchCalculateScore(studentIds2, growthItem2, yearId, semesterId, DateUtil.date(batchCode));

    }

    @Override
    public IPage<RecMilitaryVO> getRecMilitaryPage(Page<RecMilitaryVO> page, RecMilitaryDTO query) {
        return baseMapper.getRecMilitaryPage(page, query);
    }
}
