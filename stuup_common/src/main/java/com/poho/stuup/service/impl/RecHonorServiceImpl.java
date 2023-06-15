package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecHonorMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecHonorDTO;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.vo.RecHonorVO;
import com.poho.stuup.service.RecHonorService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private YearMapper yearMapper;

    @Resource
    private RecScoreService recScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecHonorExcel(long batchCode, GrowthItem growthItem, List<RecHonorExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Year currYear = yearMapper.findCurrYear();
        List<RecDefault> recDefaults = new ArrayList<>();
        //=================保存数据=================
        List<RecHonor> recHonors = excels.stream().map(excel -> {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(currYear.getOid());
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaults.add(recDefault);
            //===================================================================
            RecHonor recHonor = new RecHonor();
            recHonor.setYearId(currYear.getOid());
            recHonor.setGrowId(growthItem.getId());
            recHonor.setStudentId(excel.getStudentId());
            recHonor.setName(excel.getName());
            recHonor.setLevel(RecLevelEnum.getValueForLabel(excel.getLevel()));
            recHonor.setUnit(excel.getUnit());
            recHonor.setTime(DateUtil.parseDate(excel.getTime()));
            recHonor.setBatchCode(batchCode);
            return recHonor;
        }).collect(Collectors.toList());
        this.saveBatch(recHonors);
        // 插入一条导入日志
        RecLog recLog = new RecLog();
        recLog.setGrowId(growthItem.getId());
        recLog.setYearId(currYear.getOid());
        recLog.setCreateUser(Long.valueOf(userId));
        recLog.setBatchCode(batchCode);
        recLogMapper.insert(recLog);
        // 计算学生成长积分
        recScoreService.calculateScore(recDefaults, currYear.getOid(), growthItem);
    }

    @Override
    public IPage<RecHonorVO> getRecHonorPage(Page<RecHonorVO> page, RecHonorDTO query) {
        return baseMapper.getRecHonorPage(page, query);
    }
}
