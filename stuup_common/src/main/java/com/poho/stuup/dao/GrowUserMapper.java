package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.GrowUser;
import com.poho.stuup.model.vo.GrowthItemUserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 项目负责人表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Mapper
public interface GrowUserMapper extends BaseMapper<GrowUser> {

    /**
     * 查找项目负责人
     */
    List<Long> findGrowUser(@Param("growthItemId") Long growthItemId);

    /**
     * 查找用户项目
     */
    List<Long> findUserGrow(@Param("userId") Long userId);

    /**
     * 查询项目负责人
     */
    List<GrowthItemUserVO> getGrowItemUser(@Param("growthItemId") Long growthItemId);
}
