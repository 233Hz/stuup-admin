package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.dto.RecLogDTO;
import com.poho.stuup.model.vo.RecLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 项目记录日志 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-01
 */
@Mapper
public interface RecLogMapper extends BaseMapper<RecLog> {

    IPage<RecLogVO> getRecLogPage(Page<RecLogVO> page, @Param("query") RecLogDTO query);
}
