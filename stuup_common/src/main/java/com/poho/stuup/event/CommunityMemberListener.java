package com.poho.stuup.event;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.ProjectConstants;
import com.poho.stuup.constant.SyncCommunityMemberStateEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.SyncCommunityMemberMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.SyncCommunityMember;
import com.poho.stuup.model.dto.SocietySaveDTO;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.service.RecSocietyService;
import com.poho.stuup.service.SyncCommunityMemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CommunityMemberListener implements ApplicationListener<CommunityMemberEvent> {

    private final SyncCommunityMemberService syncCommunityMemberService;

    private final RecSocietyService recSocietyService;

    private final GrowthItemService growthItemService;

    private final YearMapper yearMapper;

    private final StudentMapper studentMapper;

    private final SyncCommunityMemberMapper syncCommunityMemberMapper;
    @Override
    @Async
    public void onApplicationEvent(CommunityMemberEvent event) {
        if (null == event) {
            return;
        }
        Integer infoId = event.getInfoId();
        List<SyncCommunityMember> memberList = syncCommunityMemberService.list(
                Wrappers.<SyncCommunityMember>lambdaQuery()
                        .eq(SyncCommunityMember::getSyncInfoId, infoId)
                        .eq(SyncCommunityMember::getState, SyncCommunityMemberStateEnum.HANDLER_WAIT) );
        if(CollUtil.isEmpty(memberList)){
            log.info(StrUtil.format("infoId:{} 本次没有待处理或处理失败记录。", infoId));
            return;
        }
        //调用积分功能模块
        long batchCode = System.currentTimeMillis();
        Long currYearId = yearMapper.findCurrYearId();
        GrowthItem growthItem = growthItemService.getOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, ProjectConstants.COMMUNITY_CODE));
        if(growthItem == null){
            log.error(StrUtil.format("本次处理社团同步的社团记录 infoId:{} communityCode:{} 系统找到该项目，请联系管理员确认"
                    , infoId, ProjectConstants.COMMUNITY_CODE));
            return;
        }
        String stuNo;
        for (SyncCommunityMember communityMember : memberList) {
            stuNo = communityMember.getStuNo();
            Long stuId = studentMapper.findStudentId(stuNo);
            if(stuId != null){
                SocietySaveDTO societySaveDTO = SocietySaveDTO.builder()
                        .communityMemberId(communityMember.getId())
                        .batchCode(batchCode)
                        .currYearId(currYearId)
                        .growthItem(growthItem)
                        .stuId(stuId)
                        .communityName(communityMember.getCommunityName())
                        .build();
                recSocietyService.saveSocietyFromSyncData(societySaveDTO);
            } else {
                log.warn(StrUtil.format("同步过来的社团成员记录 stuNo:{} stuName:{} 学号在本系统中找不到， 请联系管理员确认，是否与数据中心同步过数据"
                        , stuNo, communityMember.getStuName()));
                //更新同步记录的处理状态
                SyncCommunityMember syncCommunityMember = SyncCommunityMember.builder()
                        .id(communityMember.getId())
                        .state(SyncCommunityMemberStateEnum.HANDLER_FAIL.getValue())
                        .memo("学号在本系统中找不到， 请联系管理员确认，是否与数据中心同步过数据")
                        .updateTime(null)
                        .build();
                syncCommunityMemberMapper.updateById(syncCommunityMember);

            }
        }

    }
}
