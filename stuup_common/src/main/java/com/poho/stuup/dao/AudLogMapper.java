package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.stuup.model.AudLog;
import com.poho.stuup.model.dto.AudLogDTO;
import com.poho.stuup.model.vo.AudLogVO;
import com.poho.stuup.model.vo.AuditLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 审核日志表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Mapper
public interface AudLogMapper extends BaseMapper<AudLog> {

    IPage<AudLogVO> getAuditRecordLog(Page<AudLogVO> page, @Param("query") AudLogDTO query);

    List<AuditLogVO> getAuditLog(@Param("audId") Long audId);
}
