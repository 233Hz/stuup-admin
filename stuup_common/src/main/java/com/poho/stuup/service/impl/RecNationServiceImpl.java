package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecNationMapper;
import com.poho.stuup.model.RecNation;
import com.poho.stuup.model.excel.RecNationExcel;
import com.poho.stuup.service.RecNationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 参加国防民防项目记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecNationServiceImpl extends ServiceImpl<RecNationMapper, RecNation> implements RecNationService {

    @Override
    public boolean saveData(RecNationExcel data) {
        //=================转换数据=================
        RecNation recNation = new RecNation();
        recNation.setStudentId(data.getStudentId());
        recNation.setName(data.getName());
        recNation.setLevel(RecLevelEnum.getLabelValue(data.getLevel()));
        recNation.setOrg(data.getOrg());
        recNation.setHour(Integer.valueOf(data.getHour()));
        recNation.setBatchCode(data.getBatchCode());
        //=================插入数据=================
        return baseMapper.insert(recNation) > 0;
    }
}
