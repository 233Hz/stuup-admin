package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecDefaultMapper;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.dto.RecLogDTO;
import com.poho.stuup.model.vo.RecLogDetailsVO;
import com.poho.stuup.model.vo.RecLogVO;
import com.poho.stuup.service.RecLogService;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 项目记录日志 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-01
 */
@Service
public class RecLogServiceImpl extends ServiceImpl<RecLogMapper, RecLog> implements RecLogService {

    @Resource
    private RecDefaultMapper recDefaultMapper;

    @Override
    public IPage<RecLogVO> getRecLogPage(Page<RecLogVO> page, RecLogDTO query) {
        boolean superAdmin = Utils.isSuperAdmin(query.getUserId());
        if (superAdmin) query.setUserId(null);
        return baseMapper.getRecLogPage(page, query);
    }

    @Override
    public List<RecLogDetailsVO> getRecLogDetails(Long id) {
        return recDefaultMapper.getRecLogDetails(id);
    }
}
