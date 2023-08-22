package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecHonorMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecHonor;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.dto.RecHonorDTO;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.vo.RecHonorVO;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecHonorService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 人荣誉记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecHonorServiceImpl extends ServiceImpl<RecHonorMapper, RecHonor> implements RecHonorService {

    @Resource
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecHonorExcel(List<RecHonorExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode) {
        List<Long> studentIds = new ArrayList<>();
        for (RecHonorExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaultMapper.insert(recDefault);
            RecHonor recHonor = new RecHonor();
            recHonor.setYearId(yearId);
            recHonor.setSemesterId(semesterId);
            recHonor.setGrowId(growthItem.getId());
            recHonor.setStudentId(excel.getStudentId());
            recHonor.setName(excel.getName());
            recHonor.setLevel(RecLevelEnum.getValueForLabel(excel.getLevel()));
            recHonor.setUnit(excel.getUnit());
            recHonor.setTime(DateUtil.parseDate(excel.getTime()));
            recHonor.setBatchCode(batchCode);
            baseMapper.insert(recHonor);
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

    @Override
    public IPage<RecHonorVO> getRecHonorPage(Page<RecHonorVO> page, RecHonorDTO query) {
        return baseMapper.getRecHonorPage(page, query);
    }
}
