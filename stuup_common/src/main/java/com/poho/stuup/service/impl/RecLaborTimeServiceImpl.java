package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLaborTimeMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLaborTime;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.dto.RecLaborTimeDTO;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.model.vo.RecLaborTimeVO;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecLaborTimeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 生产劳动实践记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecLaborTimeServiceImpl extends ServiceImpl<RecLaborTimeMapper, RecLaborTime> implements RecLaborTimeService {

    @Resource
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecLaborTimeExcel(List<RecLaborTimeExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode) {
        List<Long> studentIds = new ArrayList<>();
        for (RecLaborTimeExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaultMapper.insert(recDefault);
            RecLaborTime recLaborTime = new RecLaborTime();
            recLaborTime.setYearId(yearId);
            recLaborTime.setSemesterId(semesterId);
            recLaborTime.setGrowId(growthItem.getId());
            recLaborTime.setStudentId(excel.getStudentId());
            recLaborTime.setHours(Integer.valueOf(excel.getHours()));
            recLaborTime.setBatchCode(batchCode);
            baseMapper.insert(recLaborTime);
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
    public IPage<RecLaborTimeVO> getRecLaborTimePage(Page<RecLaborTimeVO> page, RecLaborTimeDTO query) {
        return baseMapper.getRecLaborTimePage(page, query);
    }
}
