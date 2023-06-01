package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecNationMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecNation;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.RecNationExcel;
import com.poho.stuup.service.RecNationService;
import com.poho.stuup.service.RecScoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private YearMapper yearMapper;

    @Resource
    private RecScoreService recScoreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecNationExcel(long batchCode, GrowthItem growthItem, List<RecNationExcel> excels, Map<String, Object> params) {
        Year year = yearMapper.findCurrYear();
        //=================保存数据=================
        List<RecNation> recNations = excels.stream().map(excel -> {
            RecNation recNation = new RecNation();
            recNation.setYearId(year.getOid());
            recNation.setGrowId(growthItem.getId());
            recNation.setStudentId(excel.getStudentId());
            recNation.setName(excel.getName());
            recNation.setLevel(RecLevelEnum.getLabelValue(excel.getLevel()));
            recNation.setOrg(excel.getOrg());
            recNation.setHour(Integer.valueOf(excel.getHour()));
            recNation.setBatchCode(batchCode);
            return recNation;
        }).collect(Collectors.toList());
        this.saveBatch(recNations);
        // 计算学生成长积分
        List<Long> studentIds = recNations.stream().map(RecNation::getStudentId).collect(Collectors.toList());
        recScoreService.calculateScore(studentIds, growthItem);
    }
}
