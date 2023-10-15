package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecProjectMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.RecProject;
import com.poho.stuup.model.excel.RecProjectExcel;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecProjectService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 参加项目记录表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-10-13
 */
@Slf4j
@Service
@AllArgsConstructor
public class RecProjectServiceImpl extends ServiceImpl<RecProjectMapper, RecProject> implements RecProjectService {

    @Resource
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    public void saveRecVolunteerExcel(List<RecProjectExcel> excels, RecImportParams params) {
        long batchCode = System.currentTimeMillis();
        Long userId = params.getUserId();
        Long yearId = params.getYearId();
        Long semesterId = params.getSemesterId();
        GrowthItem growthItem = params.getGrowthItem();
        List<Long> studentIds = new ArrayList<>();
        for (RecProjectExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaultMapper.insert(recDefault);
            RecProject recProject = new RecProject();
            recProject.setYearId(yearId);
            recProject.setSemesterId(semesterId);
            recProject.setGrowId(growthItem.getId());
            recProject.setStudentId(excel.getStudentId());
            recProject.setName(excel.getName());
            Optional.ofNullable(excel.getName())
                    .ifPresent(recProject::setName);
            Optional.ofNullable(excel.getTime())
                    .ifPresent(time -> recProject.setTime(DateUtil.parseDate(excel.getTime())));
            Optional.ofNullable(excel.getRemark()).ifPresent(recProject::setRemark);
            recProject.setBatchCode(batchCode);
            baseMapper.insert(recProject);
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
