package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.GrowUser;
import com.poho.stuup.model.vo.SimpleUserVO;

import java.util.List;

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

    List<SimpleUserVO> getGrowItemUser(Long growId);
}
