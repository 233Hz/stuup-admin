package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.RecSocietyMapper;
import com.poho.stuup.model.RecSociety;
import com.poho.stuup.model.excel.RecSocietyExcel;
import com.poho.stuup.service.RecSocietyService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参加社团记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecSocietyServiceImpl extends ServiceImpl<RecSocietyMapper, RecSociety> implements RecSocietyService {

    @Override
    public boolean saveData(RecSocietyExcel data) {
        //=================转换数据=================
        RecSociety recSociety = new RecSociety();
        recSociety.setStudentId(data.getStudentId());
        recSociety.setName(data.getName());
        recSociety.setLevel(RecLevelEnum.getLabelValue(data.getLevel()));
        recSociety.setStartTime(DateUtil.parseDate(data.getStartTime()));
        recSociety.setEndTime(DateUtil.parseDate(data.getEndTime()));
        recSociety.setRole(RecRoleEnum.getRoleValue(data.getRole()));
        recSociety.setBatchCode(data.getBatchCode());
        //=================插入数据=================
        return baseMapper.insert(recSociety) > 0;
    }
}
