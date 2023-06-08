package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.AnnouncementPremUserDTO;
import com.poho.stuup.model.vo.AnnouncementPremUserVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface UserMapper extends BaseDao<User> {
    /**
     * @param param
     * @return
     */
    User checkUser(Map<String, Object> param);

    IPage<AnnouncementPremUserVO> getPremUser(Page<AnnouncementPremUserVO> page, @Param("query") AnnouncementPremUserDTO query);
}