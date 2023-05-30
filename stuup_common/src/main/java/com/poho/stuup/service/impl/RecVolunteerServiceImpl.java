package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecVolunteerMapper;
import com.poho.stuup.model.RecVolunteer;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.service.RecVolunteerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 志愿者活动记录填报 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-25
 */
@Service
public class RecVolunteerServiceImpl extends ServiceImpl<RecVolunteerMapper, RecVolunteer> implements RecVolunteerService {

    @Override
    public boolean saveData(RecVolunteerExcel data) {
        //=================转换数据=================
        RecVolunteer recVolunteer = new RecVolunteer();
        recVolunteer.setStudentId(data.getStudentId());
        recVolunteer.setName(data.getName());
        recVolunteer.setLevel(RecLevelEnum.getLabelValue(data.getLevel()));
        recVolunteer.setChild(data.getChild());
        recVolunteer.setPost(data.getPost());
        recVolunteer.setStudyTime(Integer.valueOf(data.getStudyTime()));
        recVolunteer.setServiceTime(DateUtil.parseDate(data.getServiceTime()));
        recVolunteer.setReason(data.getReason());
        recVolunteer.setBatchCode(data.getBatchCode());
        //=================插入数据=================
        return baseMapper.insert(recVolunteer) > 0;
    }
}
