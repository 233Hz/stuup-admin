package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecVolunteerMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecVolunteerDTO;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.model.vo.RecVolunteerVO;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.service.RecVolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecVolunteerExcel(long batchCode, GrowthItem growthItem, List<RecVolunteerExcel> excels, Map<String, Object> params) {
        String userId = (String) params.get("userId");
        Year currYear = yearMapper.findCurrYear();
        List<RecDefault> recDefaults = new ArrayList<>();
        //=================保存数据=================
        List<RecVolunteer> recVolunteers = excels.stream().map(excel -> {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(currYear.getOid());
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaults.add(recDefault);
            //===================================================================
            RecVolunteer recVolunteer = new RecVolunteer();
            recVolunteer.setYearId(currYear.getOid());
            recVolunteer.setGrowId(growthItem.getId());
            recVolunteer.setStudentId(excel.getStudentId());
            recVolunteer.setName(excel.getName());
            recVolunteer.setLevel(RecLevelEnum.getValueForLabel(excel.getLevel()));
            recVolunteer.setChild(excel.getChild());
            recVolunteer.setPost(excel.getPost());
            recVolunteer.setStudyTime(Integer.valueOf(excel.getStudyTime()));
            recVolunteer.setServiceTime(DateUtil.parseDate(excel.getServiceTime()));
            recVolunteer.setReason(excel.getReason());
            recVolunteer.setBatchCode(batchCode);
            return recVolunteer;
        }).collect(Collectors.toList());
        this.saveBatch(recVolunteers);
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
    public IPage<RecVolunteerVO> getVolunteerPage(Page<RecVolunteerVO> page, RecVolunteerDTO query) {
        return baseMapper.getVolunteerPage(page, query);
    }
}
