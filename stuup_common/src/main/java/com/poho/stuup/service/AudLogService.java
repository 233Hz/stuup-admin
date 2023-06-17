package com.poho.stuup.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.AudLog;
import com.poho.stuup.model.dto.AudLogDTO;
import com.poho.stuup.model.vo.AudLogVO;
import com.poho.stuup.model.vo.AuditLogVO;

import java.util.List;

/**
 * <p>
 * 审核日志表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
public interface AudLogService extends IService<AudLog> {

    /**
     * @description: 获取当前用户的审核日志（分页）
     * @param: page
     * @param: query
     * @return: com.baomidou.mybatisplus.core.metadata.IPage<com.poho.stuup.model.vo.AudLogVO>
     * @author BUNGA
     * @date: 2023/6/15 18:15
     */
    IPage<AudLogVO> getAuditRecordLog(Page<AudLogVO> page, AudLogDTO query);

    /**
     * @description: 获取记录的审核日志
     * @param: audId
     * @return: java.util.List<com.poho.stuup.model.vo.AuditLogVO>
     * @author BUNGA
     * @date: 2023/6/15 18:31
     */
    List<AuditLogVO> getAuditLog(Long audId);
}
