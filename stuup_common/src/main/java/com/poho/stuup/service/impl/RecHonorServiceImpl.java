package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecHonorMapper;
import com.poho.stuup.model.RecHonor;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.service.RecHonorService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人荣誉记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecHonorServiceImpl extends ServiceImpl<RecHonorMapper, RecHonor> implements RecHonorService {

    @Override
    public boolean saveData(RecHonorExcel data) {
        //=================转换数据=================
        RecHonor recHonor = new RecHonor();
        recHonor.setStudentId(data.getStudentId());
        recHonor.setName(data.getName());
        recHonor.setLevel(RecLevelEnum.getLabelValue(data.getLevel()));
        recHonor.setUnit(data.getUnit());
        recHonor.setTime(DateUtil.parseDate(data.getTime()));
        recHonor.setBatchCode(data.getBatchCode());
        //=================插入数据=================
        return baseMapper.insert(recHonor) > 0;
    }
}
