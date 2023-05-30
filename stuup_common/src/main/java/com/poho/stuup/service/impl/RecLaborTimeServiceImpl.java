package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RecLaborTimeMapper;
import com.poho.stuup.model.RecLaborTime;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.service.RecLaborTimeService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 生产劳动实践记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecLaborTimeServiceImpl extends ServiceImpl<RecLaborTimeMapper, RecLaborTime> implements RecLaborTimeService {

    @Override
    public boolean saveData(RecLaborTimeExcel data) {
        //=================转换数据=================
        RecLaborTime recLaborTime = new RecLaborTime();
        recLaborTime.setStudentId(data.getStudentId());
        recLaborTime.setHours(Integer.valueOf(data.getHours()));
        recLaborTime.setBatchCode(data.getBatchCode());
        //=================插入数据=================
        return baseMapper.insert(recLaborTime) > 0;
    }
}
