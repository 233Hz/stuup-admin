package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.vo.AnnouncementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 发布公告表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-08
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    IPage<AnnouncementVO> notifyPage(Page<AnnouncementVO> page, @Param("query") AnnouncementDTO query);

    IPage<AnnouncementVO> myNotifyPage(Page<AnnouncementVO> page, @Param("query") AnnouncementDTO query);

    IPage<AnnouncementVO> mySystemPage(Page<AnnouncementVO> page, @Param("query") AnnouncementDTO query);
}
