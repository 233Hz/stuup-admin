package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AnnouncementScopeEnum;
import com.poho.stuup.constant.AnnouncementStateEnum;
import com.poho.stuup.constant.AnnouncementTypeEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.dao.AnnouncementMapper;
import com.poho.stuup.dao.AnnouncementUserMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.vo.AnnouncementVO;
import com.poho.stuup.service.AnnouncementService;
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
    public IPage<AnnouncementVO> notifyPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        return baseMapper.notifyPage(page, query);
    }

    @Override
    public IPage<AnnouncementVO> myNotifyPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        Long userId = query.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        Integer userType = user.getUserType();
        if (userType != UserTypeEnum.TEACHER.getValue()) {
            query.setScope(AnnouncementScopeEnum.ALL.getValue());
        }
        return baseMapper.myNotifyPage(page, query);
    }

    @Override
    public IPage<AnnouncementVO> mySystemPage(Page<AnnouncementVO> page, AnnouncementDTO query) {
        return baseMapper.mySystemPage(page, query);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> saveOrUpdateNotify(AnnouncementDTO data) {
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
}
