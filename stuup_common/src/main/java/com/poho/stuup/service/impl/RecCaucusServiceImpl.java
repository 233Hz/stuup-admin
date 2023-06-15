package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecCaucusMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecCaucusDTO;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.model.vo.RecCaucusVO;
import com.poho.stuup.service.RecCaucusService;
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

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecCaucusExcel(long batchCode, GrowthItem growthItem, List<RecCaucusExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Year currYear = yearMapper.findCurrYear();
        List<RecDefault> recDefaults = new ArrayList<>();
        //=================保存数据=================
        List<RecCaucus> recCaucuses = excels.stream().map(excel -> {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(currYear.getOid());
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaults.add(recDefault);
            //===================================================================
            RecCaucus recCaucus = new RecCaucus();
            recCaucus.setYearId(currYear.getOid());
            recCaucus.setGrowId(growthItem.getId());
            recCaucus.setStudentId(excel.getStudentId());
            recCaucus.setName(excel.getName());
            recCaucus.setLevel(RecLevelEnum.getValueForLabel(excel.getLevel()));
            recCaucus.setStartTime(DateUtil.parseDate(excel.getStartTime()));
            recCaucus.setEndTime(DateUtil.parseDate(excel.getEndTime()));
            recCaucus.setBatchCode(batchCode);
            return recCaucus;
        }).collect(Collectors.toList());
        this.saveBatch(recCaucuses);
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
    public IPage<RecCaucusVO> getRecCaucusPage(Page<RecCaucusVO> page, RecCaucusDTO query) {
        return baseMapper.getRecCaucusPage(page, query);
    }
}
