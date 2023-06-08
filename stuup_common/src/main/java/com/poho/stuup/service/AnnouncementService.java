package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.dto.AnnouncementPremUserDTO;
import com.poho.stuup.model.vo.AnnouncementPremUserVO;

/**
 * <p>
 * 发布公告表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-08
 */
public interface AnnouncementService extends IService<Announcement> {

    ResponseModel<Boolean> saveOrUpdateAnnouncement(Announcement announcement);

    IPage<AnnouncementPremUserVO> getAnnouncementPremUserVO(Page<AnnouncementPremUserVO> page, AnnouncementPremUserDTO query);

    IPage<Announcement> getAnnouncementMyPage(Page<Announcement> page, AnnouncementDTO query);
}
