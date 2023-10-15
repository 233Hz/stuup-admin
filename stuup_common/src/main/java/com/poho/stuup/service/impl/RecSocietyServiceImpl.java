package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.constant.SyncCommunityMemberStateEnum;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.dao.RecSocietyMapper;
import com.poho.stuup.dao.SyncCommunityMemberMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.RecSocietyDTO;
import com.poho.stuup.model.dto.SocietySaveDTO;
import com.poho.stuup.model.excel.RecSocietyExcel;
import com.poho.stuup.model.vo.RecSocietyVO;
import com.poho.stuup.service.RecAddScoreService;
import com.poho.stuup.service.RecSocietyService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 参加社团记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecSocietyServiceImpl extends ServiceImpl<RecSocietyMapper, RecSociety> implements RecSocietyService {

    @Resource
    private RecDefaultMapper recDefaultMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private RecLogMapper recLogMapper;

    @Resource
    private SyncCommunityMemberMapper syncCommunityMemberMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRecSocietyExcel(List<RecSocietyExcel> excels, RecImportParams params) {
        long batchCode = System.currentTimeMillis();
        Long userId = params.getUserId();
        Long yearId = params.getYearId();
        Long semesterId = params.getSemesterId();
        GrowthItem growthItem = params.getGrowthItem();
        List<Long> studentIds = new ArrayList<>();
        for (RecSocietyExcel excel : excels) {
            RecDefault recDefault = new RecDefault();
            recDefault.setYearId(yearId);
            recDefault.setSemesterId(semesterId);
            recDefault.setGrowId(growthItem.getId());
            recDefault.setStudentId(excel.getStudentId());
            recDefault.setBatchCode(batchCode);
            recDefault.setRemark(excel.getRemark());
            recDefaultMapper.insert(recDefault);
            RecSociety recSociety = new RecSociety();
            recSociety.setYearId(yearId);
            recSociety.setSemesterId(semesterId);
            recSociety.setGrowId(growthItem.getId());
            recSociety.setStudentId(excel.getStudentId());
            recSociety.setName(excel.getName());
            recSociety.setLevel(RecLevelEnum.getValueForLabel(excel.getLevel()));
            recSociety.setStartTime(DateUtil.parseDate(excel.getStartTime()));
            recSociety.setEndTime(DateUtil.parseDate(excel.getEndTime()));
            recSociety.setRole(RecRoleEnum.getValueForRole(excel.getRole()));
            recSociety.setBatchCode(batchCode);
            baseMapper.insert(recSociety);
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
    @Transactional(rollbackFor = Exception.class)
    public void saveSocietyFromSyncData(SocietySaveDTO societySaveDTO) {
        Long currYearId = societySaveDTO.getCurrYearId();
        Long currSemesterId = societySaveDTO.getCurrSemesterId();
        Long stuId = societySaveDTO.getStuId();
        Long growthItemId = societySaveDTO.getGrowthItem().getId();

        RecSociety recSociety = new RecSociety();
        recSociety.setYearId(currYearId);
        recSociety.setSemesterId(currSemesterId);
        recSociety.setGrowId(growthItemId);
        recSociety.setStudentId(stuId);
        recSociety.setName(societySaveDTO.getCommunityName());
        this.save(recSociety);
        // 计算学生成长积分
        RecDefault recDefault = new RecDefault();
        recDefault.setYearId(currYearId);
        recDefault.setSemesterId(currSemesterId);
        recDefault.setGrowId(growthItemId);
        recDefault.setStudentId(stuId);
        recDefault.setBatchCode(societySaveDTO.getBatchCode());
        recDefaultMapper.insert(recDefault);
        Map<String, Object> params = new HashMap<>();
        params.put("nowTime", new Date());
        //更新同步记录的处理状态
        SyncCommunityMember syncCommunityMember = SyncCommunityMember.builder()
                .id(societySaveDTO.getCommunityMemberId())
                .memo(null)
                .updateTime(null)
                .state(SyncCommunityMemberStateEnum.HANDLER_SUCCESS.getValue())
                .build();
        syncCommunityMemberMapper.updateById(syncCommunityMember);
    }

    @Override
    public IPage<RecSocietyVO> getRecSocietyPage(Page<RecSocietyVO> page, RecSocietyDTO query) {
        return baseMapper.getRecSocietyPage(page, query);
    }

}
