package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecSocietyMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecSocietyDTO;
import com.poho.stuup.model.excel.RecSocietyExcel;
import com.poho.stuup.model.vo.RecSocietyVO;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.RecSocietyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 参加社团记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecSocietyServiceImpl extends ServiceImpl<RecSocietyMapper, RecSociety> implements RecSocietyService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private RecScoreService recScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecSocietyExcel(long batchCode, GrowthItem growthItem, List<RecSocietyExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Year currYear = yearMapper.findCurrYear();
        List<RecDefault> recDefaults = new ArrayList<>();
        //=================保存数据=================
        List<RecSociety> recSocieties = excels.stream().map(excel -> {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(currYear.getOid());
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaults.add(recDefault);
            //===================================================================
            RecSociety recSociety = new RecSociety();
            recSociety.setYearId(currYear.getOid());
            recSociety.setGrowId(growthItem.getId());
            recSociety.setStudentId(excel.getStudentId());
            recSociety.setName(excel.getName());
            recSociety.setLevel(RecLevelEnum.getLabelValue(excel.getLevel()));
            recSociety.setStartTime(DateUtil.parseDate(excel.getStartTime()));
            recSociety.setEndTime(DateUtil.parseDate(excel.getEndTime()));
            recSociety.setRole(RecRoleEnum.getRoleValue(excel.getRole()));
            recSociety.setBatchCode(batchCode);
            return recSociety;
        }).collect(Collectors.toList());
        this.saveBatch(recSocieties);
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
    public IPage<RecSocietyVO> getRecSocietyPage(Page<RecSocietyVO> page, RecSocietyDTO query) {
        return baseMapper.getRecSocietyPage(page, query);
    }

}
