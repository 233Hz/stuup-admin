package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.AudLogMapper;
import com.poho.stuup.model.AudLog;
import com.poho.stuup.model.dto.AudLogDTO;
import com.poho.stuup.model.vo.AudLogVO;
import com.poho.stuup.model.vo.AuditLogVO;
import com.poho.stuup.service.AudLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 审核日志表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Service
public class AudLogServiceImpl extends ServiceImpl<AudLogMapper, AudLog> implements AudLogService {

    @Override
    public IPage<AudLogVO> getAuditRecordLog(Page<AudLogVO> page, AudLogDTO query) {
        return baseMapper.getAuditRecordLog(page, query);
    }

    @Override
    public List<AuditLogVO> getAuditLog(Long audId) {
        return baseMapper.getAuditLog(audId);
    }
}
