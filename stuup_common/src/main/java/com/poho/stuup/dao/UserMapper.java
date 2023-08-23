package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.AnnouncementPremUserDTO;
import com.poho.stuup.model.dto.SimpleUserDTO;
import com.poho.stuup.model.vo.AnnouncementPremUserVO;
import com.poho.stuup.model.vo.SimpleUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseDao<User> {
    /**
     * @param param
     * @return
     */
    User checkUser(Map<String, Object> param);

    IPage<AnnouncementPremUserVO> getPremUser(Page<AnnouncementPremUserVO> page, @Param("query") AnnouncementPremUserDTO query);

    List<SimpleUserVO> getSimpleUserPage(@Param("current") long current, @Param("size") long size, @Param("query") SimpleUserDTO query);

    List<User> getAllUserLoginNamesAndIds();

    int updateUserAvatar(@Param("userId") Long userId, @Param("avatarId") Long avatarId);
}