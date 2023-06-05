package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecLaborTimeMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecLaborTime;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.dto.RecLaborTimeDTO;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.model.vo.RecLaborTimeVO;
import com.poho.stuup.service.RecLaborTimeService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private YearMapper yearMapper;
    @Resource
    private RecScoreService recScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecLaborTimeExcel(long batchCode, GrowthItem growthItem, List<RecLaborTimeExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Year currYear = yearMapper.findCurrYear();
        //=================保存数据=================
        List<RecLaborTime> recLaborTimes = excels.stream().map(excel -> {
            RecLaborTime recLaborTime = new RecLaborTime();
            recLaborTime.setYearId(currYear.getOid());
            recLaborTime.setGrowId(growthItem.getId());
            recLaborTime.setStudentId(excel.getStudentId());
            recLaborTime.setHours(Integer.valueOf(excel.getHours()));
            recLaborTime.setBatchCode(batchCode);
            return recLaborTime;
        }).collect(Collectors.toList());
        this.saveBatch(recLaborTimes);
        // 插入一条导入日志
        RecLog recLog = new RecLog();
        recLog.setGrowId(growthItem.getId());
        recLog.setYearId(currYear.getOid());
        recLog.setCreateUser(Long.valueOf(userId));
        recLog.setBatchCode(batchCode);
        recLogMapper.insert(recLog);
        // 计算学生成长积分
        List<Long> studentIds = recLaborTimes.stream().map(RecLaborTime::getStudentId).collect(Collectors.toList());
        recScoreService.calculateScore(studentIds, currYear.getOid(), growthItem);
    }

    @Override
    public IPage<RecLaborTimeVO> getRecLaborTimePage(Page<RecLaborTimeVO> page, RecLaborTimeDTO query) {
        return baseMapper.getRecLaborTimePage(page, query);
    }
}
