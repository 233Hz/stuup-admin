package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecCaucusMapper;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecCaucus;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.dto.RecCaucusDTO;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.model.vo.RecCaucusVO;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecCaucusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecCaucusExcel(List<RecCaucusExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode) {
        List<Long> studentIds = new ArrayList<>();
        for (RecCaucusExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaultMapper.insert(recDefault);
            RecCaucus recCaucus = new RecCaucus();
            recCaucus.setYearId(yearId);
            recCaucus.setSemesterId(semesterId);
            recCaucus.setGrowId(growthItem.getId());
            recCaucus.setStudentId(excel.getStudentId());
            recCaucus.setName(excel.getName());
            recCaucus.setLevel(RecLevelEnum.getValueForLabel(excel.getLevel()));
            recCaucus.setStartTime(DateUtil.parseDate(excel.getStartTime()));
            recCaucus.setEndTime(DateUtil.parseDate(excel.getEndTime()));
            recCaucus.setBatchCode(batchCode);
            baseMapper.insert(recCaucus);
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
    public IPage<RecCaucusVO> getRecCaucusPage(Page<RecCaucusVO> page, RecCaucusDTO query) {
        return baseMapper.getRecCaucusPage(page, query);
    }
}
