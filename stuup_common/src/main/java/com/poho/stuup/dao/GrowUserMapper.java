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
     * @description: 查找项目负责人
     * @param: batchCode
     * @return: java.util.List<java.lang.Long>
     * @author BUNGA
     * @date: 2023/5/29 15:52
     */
    List<Long> findGrowUser(@Param("growthItemId") Long growthItemId);

    /**
     * @description: 查找用户项目
     * @param: userId
     * @return: java.util.List<java.lang.Long>
     * @author BUNGA
     * @date: 2023/5/30 18:45
     */
    List<Long> findUserGrow(@Param("userId") Long userId);

    /**
     * @description: 查询项目负责人
     * @param: growthItemId
     * @return: java.util.List<com.poho.stuup.model.vo.GrowthItemUserVO>
     * @author BUNGA
     * @date: 2023/6/15 11:11
     */
    List<GrowthItemUserVO> getGrowItemUser(@Param("growthItemId") Long growthItemId);
}
