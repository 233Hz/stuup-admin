package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.service.RecDefaultService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private YearMapper yearMapper;

    @Resource
    private RecScoreService recScoreService;

    @Resource
    private RecLogMapper recLogMapper;


    @Override
    public void saveRecDefaultExcel(long batchCode, GrowthItem growthItem, List<RecDefaultExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Year currYear = yearMapper.findCurrYear();
        if (currYear == null) throw new RuntimeException("不在学年时间范围内，无法导入");
        // 保存导入记录
        List<RecDefault> recDefaults = excels.stream().map(excel -> {
            RecDefault recDefault = new RecDefault();
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setYearId(currYear.getOid());
            recDefault.setGrowId(growthItem.getId());
            recDefault.setRemark(excel.getRemark());
            recDefault.setBatchCode(batchCode);
            return recDefault;
        }).collect(Collectors.toList());
        // 插入一条导入日志
        RecLog recLog = new RecLog();
        recLog.setGrowId(growthItem.getId());
        recLog.setYearId(currYear.getOid());
        recLog.setCreateUser(Long.valueOf(userId));
        recLog.setBatchCode(batchCode);
        recLogMapper.insert(recLog);
        // 计算学生成长积分
        recScoreService.calculateScore(recDefaults, currYear.getOid(), growthItem, params);
    }

}
