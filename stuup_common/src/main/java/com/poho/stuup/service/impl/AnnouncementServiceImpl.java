package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AnnouncementScopeEnum;
import com.poho.stuup.dao.AnnouncementMapper;
import com.poho.stuup.dao.AnnouncementUserMapper;
import com.poho.stuup.dao.UserMapper;
import com.poho.stuup.model.Announcement;
import com.poho.stuup.model.AnnouncementUser;
import com.poho.stuup.model.dto.AnnouncementDTO;
import com.poho.stuup.model.dto.AnnouncementPremUserDTO;
import com.poho.stuup.model.vo.AnnouncementPremUserVO;
import com.poho.stuup.service.AnnouncementService;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> saveOrUpdateAnnouncement(Announcement announcement) {
        boolean hasId = (announcement.getId() != null);
        boolean result;
        if (hasId) {
            result = this.update(Wrappers.<Announcement>lambdaUpdate()
                    .set(Announcement::getContent, announcement.getContent()));
        } else {
            Integer type = announcement.getType();
            List<Long> userIds = announcement.getUserIds();
            if (type == AnnouncementScopeEnum.DESIGNATED.getValue() && CollUtil.isEmpty(userIds))
                return ResponseModel.failed("请选择要发布公告的指定用户");
            result = this.save(announcement);
            if (CollUtil.isNotEmpty(userIds)) {
                for (Long userId : userIds) {
                    AnnouncementUser announcementUser = new AnnouncementUser();
                    announcementUser.setAnnouncementId(announcement.getId());
                    announcementUser.setUserId(userId);
                    announcementUserMapper.insert(announcementUser);
                }
            }
        }
        return result ? ResponseModel.ok(true, hasId ? "更新成功" : "新增成功") : ResponseModel.failed(false, hasId ? "更新失败" : "新增失败");
    }

    @Override
    public IPage<AnnouncementPremUserVO> getAnnouncementPremUserVO(Page<AnnouncementPremUserVO> page, AnnouncementPremUserDTO query) {
        boolean superAdmin = Utils.isSuperAdmin(query.getCurrUser());
        if (superAdmin) {
            query.setCurrUser(null);
        }
        return userMapper.getPremUser(page, query);
    }

    @Override
    public IPage<Announcement> getAnnouncementMyPage(Page<Announcement> page, AnnouncementDTO query) {
        return baseMapper.getAnnouncementMyPage(page, query);
    }
}
