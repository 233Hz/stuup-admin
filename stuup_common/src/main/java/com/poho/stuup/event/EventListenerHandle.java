package com.poho.stuup.event;

import com.poho.stuup.constant.AnnouncementStateEnum;
import com.poho.stuup.constant.AnnouncementTypeEnum;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.AnnouncementUser;
import com.poho.stuup.model.dto.SystemMagVO;
import com.poho.stuup.service.AnnouncementService;
import com.poho.stuup.service.AnnouncementUserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author BUNGA
 * @description: 事件监听器处理
 * @date 2023/6/12 14:35
 */

@Slf4j
@Component
public class EventListenerHandle {


    @Resource
    private AnnouncementService announcementService;

    @Resource
    private AnnouncementUserService announcementUserService;


    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handle(@NotNull SystemMsgEvent event) {
        SystemMagVO systemMagVO = event.getSystemMagVO();
        Announcement announcement = new Announcement();
        announcement.setTitle(systemMagVO.getTitle());
        announcement.setType(AnnouncementTypeEnum.SYSTEM.getValue());
        announcement.setState(AnnouncementStateEnum.PUBLISHED.getValue());
        announcementService.save(announcement);
        AnnouncementUser announcementUser = new AnnouncementUser();
        announcementUser.setAnnouncementId(announcement.getId());
        announcementUser.setUserId(systemMagVO.getUserId());
        announcementUserService.save(announcementUser);
    }

}
