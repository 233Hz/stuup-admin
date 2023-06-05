package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecEnum;
import com.poho.stuup.dao.RecLogMapper;
import com.poho.stuup.handle.RecDefaultHandle;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.RecLog;
import com.poho.stuup.model.dto.RecLogDTO;
import com.poho.stuup.model.vo.RecLogDetailsVO;
import com.poho.stuup.model.vo.RecLogVO;
import com.poho.stuup.service.GrowthItemService;
import com.poho.stuup.service.RecLogService;
import com.poho.stuup.util.Utils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    private GrowthItemService growthItemService;

    @Override
    public IPage<RecLogVO> getRecLogPage(Page<RecLogVO> page, RecLogDTO query) {
        boolean superAdmin = Utils.isSuperAdmin(query.getUserId());
        if (superAdmin) query.setUserId(null);
        return baseMapper.getRecLogPage(page, query);
    }

    @Override
    public RecLogDetailsVO getRecLogDetails(Long id) {
        RecLog recLog = this.getById(id);
        Long growId = recLog.getGrowId();
        GrowthItem growthItem = growthItemService.getById(growId);
        String recCode = growthItem.getCode();
        RecEnum recEnum = RecEnum.getByCode(recCode);
        if (recEnum == null) {
            RecDefaultHandle recDefaultHandle = new RecDefaultHandle();
        }
        Long batchCode = recLog.getBatchCode();

        return null;
    }
}
