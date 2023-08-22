package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.StuGrowthMapper;
import com.poho.stuup.model.StuGrowth;
import com.poho.stuup.service.StuGrowthService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生成长项目相关信息 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-08-04
 */
@Service
public class StuGrowthServiceImpl extends ServiceImpl<StuGrowthMapper, StuGrowth> implements StuGrowthService {

    @Override
    public void addCollectCount(Long studentId, Long growthItemId) {
        StuGrowth stuGrowth = baseMapper.selectOne(Wrappers.<StuGrowth>lambdaQuery()
                .eq(StuGrowth::getStudentId, studentId)
                .eq(StuGrowth::getGrowId, growthItemId));
        if (stuGrowth == null) {
            stuGrowth = new StuGrowth();
            stuGrowth.setStudentId(studentId);
            stuGrowth.setGrowId(growthItemId);
            stuGrowth.setCount(1);
            baseMapper.insert(stuGrowth);
        } else {
            this.update(Wrappers.<StuGrowth>lambdaUpdate()
                    .set(StuGrowth::getCount, stuGrowth.getCount() + 1)
                    .eq(StuGrowth::getStudentId, studentId)
                    .eq(StuGrowth::getGrowId, growthItemId));
        }
    }
}
