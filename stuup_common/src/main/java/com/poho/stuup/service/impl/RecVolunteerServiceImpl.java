package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecVolunteerMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecDefault;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.RecVolunteer;
import com.poho.stuup.model.dto.RecVolunteerDTO;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.model.vo.RecVolunteerVO;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecVolunteerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecVolunteerExcel(List<RecVolunteerExcel> excels, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, Long batchCode) {
        List<Long> studentIds = new ArrayList<>();
        for (RecVolunteerExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaultMapper.insert(recDefault);
            RecVolunteer recVolunteer = new RecVolunteer();
            recVolunteer.setYearId(yearId);
            recVolunteer.setSemesterId(semesterId);
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
            baseMapper.insert(recVolunteer);
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
    public IPage<RecVolunteerVO> getVolunteerPage(Page<RecVolunteerVO> page, RecVolunteerDTO query) {
        return baseMapper.getVolunteerPage(page, query);
    }
}
