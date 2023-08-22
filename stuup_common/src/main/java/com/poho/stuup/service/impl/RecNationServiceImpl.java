package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecNationMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.RecNation;
import com.poho.stuup.model.dto.RecNationDTO;
import com.poho.stuup.model.excel.RecNationExcel;
import com.poho.stuup.model.vo.RecNationVO;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecNationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 参加国防民防项目记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecNationServiceImpl extends ServiceImpl<RecNationMapper, RecNation> implements RecNationService {

    @Resource
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecNationExcel(List<RecNationExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode) {
        List<Long> studentIds = new ArrayList<>();
        for (RecNationExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaultMapper.insert(recDefault);
            RecNation recNation = new RecNation();
            recNation.setYearId(yearId);
            recNation.setSemesterId(semesterId);
            recNation.setGrowId(growthItem.getId());
            recNation.setStudentId(excel.getStudentId());
            recNation.setName(excel.getName());
            recNation.setLevel(RecLevelEnum.getValueForLabel(excel.getLevel()));
            recNation.setOrg(excel.getOrg());
            recNation.setHour(Integer.valueOf(excel.getHour()));
            recNation.setBatchCode(batchCode);
            baseMapper.insert(recNation);
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
    public IPage<RecNationVO> getRecNationPage(Page<RecNationVO> page, RecNationDTO query) {
        return baseMapper.getRecNationPage(page, query);
    }
}
