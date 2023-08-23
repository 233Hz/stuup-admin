package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.LoginLog;
import com.poho.stuup.model.vo.DailyVisitsVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户登入日志 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-22
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

    int findTodayLoginCount(Long userId);

    List<DailyVisitsVO> countDailyVisits();
}
