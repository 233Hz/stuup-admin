package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecVolunteerMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecVolunteer;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.RecVolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 志愿者活动记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecVolunteerServiceImpl extends ServiceImpl<RecVolunteerMapper, RecVolunteer> implements RecVolunteerService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private RecScoreService recScoreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecVolunteerExcel(long batchCode, GrowthItem growthItem, List<RecVolunteerExcel> excels, Map<String, Object> params) {
        Year year = yearMapper.findCurrYear();
        //=================保存数据=================
        List<RecVolunteer> recVolunteers = excels.stream().map(excel -> {
            RecVolunteer recVolunteer = new RecVolunteer();
            recVolunteer.setYearId(year.getOid());
            recVolunteer.setGrowId(growthItem.getId());
            recVolunteer.setStudentId(excel.getStudentId());
            recVolunteer.setName(excel.getName());
            recVolunteer.setLevel(RecLevelEnum.getLabelValue(excel.getLevel()));
            recVolunteer.setChild(excel.getChild());
            recVolunteer.setPost(excel.getPost());
            recVolunteer.setStudyTime(Integer.valueOf(excel.getStudyTime()));
            recVolunteer.setServiceTime(DateUtil.parseDate(excel.getServiceTime()));
            recVolunteer.setReason(excel.getReason());
            recVolunteer.setBatchCode(batchCode);
            return recVolunteer;
        }).collect(Collectors.toList());
        this.saveBatch(recVolunteers);
        // 计算学生成长积分
        List<Long> studentIds = recVolunteers.stream().map(RecVolunteer::getStudentId).collect(Collectors.toList());
        recScoreService.calculateScore(studentIds, growthItem);
    }
}
