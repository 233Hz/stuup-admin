package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.vo.AnnouncementVO;

/**
 * <p>
 * 发布公告表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-08
 */
public interface AnnouncementService extends IService<Announcement> {

    IPage<AnnouncementVO> notifyPage(Page<AnnouncementVO> page, AnnouncementDTO query);

    IPage<AnnouncementVO> myNotifyPage(Page<AnnouncementVO> page, AnnouncementDTO query);

    IPage<AnnouncementVO> mySystemPage(Page<AnnouncementVO> page, AnnouncementDTO query);

    ResponseModel<Boolean> saveOrUpdateNotify(AnnouncementDTO data);

}
