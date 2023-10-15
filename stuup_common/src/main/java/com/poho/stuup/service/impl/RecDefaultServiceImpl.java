package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecDefaultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 默认积分记录表（除综评表） 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-26
 */
@Service
public class RecDefaultServiceImpl extends ServiceImpl<RecDefaultMapper, RecDefault> implements RecDefaultService {

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecDefaultExcel(List<RecDefaultExcel> excels, RecImportParams params) {
        long batchCode = System.currentTimeMillis();
        Long userId = params.getUserId();
        Long yearId = params.getYearId();
        Long semesterId = params.getSemesterId();
        GrowthItem growthItem = params.getGrowthItem();
        List<Long> studentIds = new ArrayList<>();
        for (RecDefaultExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setRemark(excel.getRemark());
            recDefault.setBatchCode(batchCode);
            baseMapper.insert(recDefault);
            studentIds.add(excel.getStudentId());
        }
        RecLog recLog = new RecLog();
        recLog.setYearId(yearId);
        recLog.setSemesterId(semesterId);
        recLog.setGrowId(growthItem.getId());
        recLog.setCreateUser(userId);
        recLog.setBatchCode(batchCode);
        recLogMapper.insert(recLog);
        recAddScoreService.batchCalculateScore(studentIds, growthItem, yearId, semesterId, DateUtil.date(batchCode));
    }

}
