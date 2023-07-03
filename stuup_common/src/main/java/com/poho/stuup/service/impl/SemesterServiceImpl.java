package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.SemesterMapper;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.dto.SemesterDTO;
import com.poho.stuup.model.vo.SemesterVO;
import com.poho.stuup.service.SemesterService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 学期管理表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@Service
public class SemesterServiceImpl extends ServiceImpl<SemesterMapper, Semester> implements SemesterService {

    @Override
    public IPage<SemesterVO> pageSemester(Page<SemesterVO> page, SemesterDTO query) {
        return baseMapper.pageSemester(page, query);
    }

    @Override
    public Long getCurrentSemesterId() {
        return baseMapper.getCurrentSemesterId();
    }

    @Override
    public Semester getCurrentSemester() {
        return baseMapper.selectOne(Wrappers.<Semester>lambdaQuery().eq(Semester::getIsCurrent, WhetherEnum.YES.getValue()));
    }

    @Override
    public Semester findTimeBelongYear(Date date) {
        return baseMapper.findTimeBelongYear(date);
    }

}
