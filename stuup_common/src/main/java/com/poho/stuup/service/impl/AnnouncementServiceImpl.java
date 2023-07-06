package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AnnouncementStateEnum;
import com.poho.stuup.constant.AnnouncementTypeEnum;
import com.poho.stuup.dao.AnnouncementMapper;
import com.poho.stuup.dao.AnnouncementUserMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.dto.AnnouncementPremUserDTO;
import com.poho.stuup.model.vo.AnnouncementPremUserVO;
import com.poho.stuup.model.vo.AnnouncementVO;
import com.poho.stuup.service.AnnouncementService;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 * 发布公告表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-08
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AnnouncementUserMapper announcementUserMapper;

    @Override
    public IPage<AnnouncementVO> getAnnouncementPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        return baseMapper.getAnnouncementPage(page, query);
    }

    @Override
    public IPage<AnnouncementVO> getMyAnnouncementPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        return baseMapper.getMyAnnouncementPage(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> saveOrUpdateAnnouncement(AnnouncementDTO data) {
        String message = "保存";
        Announcement announcement = new Announcement();
        announcement.setId(data.getId());
        announcement.setTitle(data.getTitle());
        announcement.setContent(data.getContent());
        announcement.setCreateUser(data.getUserId());
        announcement.setType(AnnouncementTypeEnum.ANNOUNCEMENT.getValue());
        announcement.setScope(data.getScope());
        if (data.isPublish()) {
            announcement.setState(AnnouncementStateEnum.PUBLISHED.getValue());
            message = "发布";
        }
        this.saveOrUpdate(announcement);
        announcementUserMapper.saveAnnouncementUser(announcement.getId(), data.getScope());
        return ResponseModel.ok(null, StrUtil.format("{}成功", message));
    }

    @Override
    public IPage<AnnouncementPremUserVO> getAnnouncementPremUserVO(Page<AnnouncementPremUserVO> page, AnnouncementPremUserDTO query) {
        boolean superAdmin = Utils.isSuperAdmin(query.getCurrUser());
        if (superAdmin) {
            query.setCurrUser(null);
        }
        return userMapper.getPremUser(page, query);
    }
}
