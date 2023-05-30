package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.GrowUserMapper;
import com.poho.stuup.model.GrowUser;
import com.poho.stuup.service.GrowUserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 项目负责人表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Service
public class GrowUserServiceImpl extends ServiceImpl<GrowUserMapper, GrowUser> implements GrowUserService {

    @Override
    public boolean isGrowUser(Long userId, Long growId) {
        List<Long> userIds = baseMapper.findGrowUser(growId);
        return userIds.contains(userId);
    }
}
