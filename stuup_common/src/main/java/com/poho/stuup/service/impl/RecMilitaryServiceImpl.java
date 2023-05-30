package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.MilitaryLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.RecMilitaryMapper;
import com.poho.stuup.model.RecMilitary;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.service.RecMilitaryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 军事训练记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecMilitaryServiceImpl extends ServiceImpl<RecMilitaryMapper, RecMilitary> implements RecMilitaryService {

    @Override
    public boolean saveData(RecMilitaryExcel data) {
        //=================转换数据=================
        RecMilitary recMilitary = new RecMilitary();
        recMilitary.setStudentId(data.getStudentId());
        recMilitary.setLevel(MilitaryLevelEnum.getLabelValue(data.getLevel()));
        recMilitary.setExcellent(WhetherEnum.getLabelValue(data.getExcellent()));
        recMilitary.setBatchCode(data.getBatchCode());
        //=================插入数据=================
        return baseMapper.insert(recMilitary) > 0;
    }
}
