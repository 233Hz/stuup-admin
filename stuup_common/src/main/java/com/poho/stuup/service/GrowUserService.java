package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowUser;

/**
 * <p>
 * 项目负责人表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface GrowUserService extends IService<GrowUser> {

    boolean isGrowUser(Long userId, Long growId);
}
