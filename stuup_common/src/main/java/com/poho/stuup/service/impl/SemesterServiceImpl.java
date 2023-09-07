package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Grade;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.dto.SemesterDTO;
import com.poho.stuup.model.vo.SemesterVO;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.SemesterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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

    @Resource
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private YearMapper yearMapper;

    @Resource
    private GradeMapper gradeMapper;

    @Resource
    private IConfigService configService;

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

    @Override
    public List<Semester> getStudentSemester(Integer studentId) {
        Student student = studentMapper.selectByPrimaryKey(studentId);
        if (student == null) return null;
        Integer gradeId = student.getGradeId();
        Grade grade = gradeMapper.selectByPrimaryKey(gradeId);
        if (grade == null) return null;
        String year = grade.getYear();
        return baseMapper.selectList(Wrappers.<Semester>lambdaQuery()
                .select(Semester::getId, Semester::getName, Semester::getIsCurrent)
                .ge(Semester::getYear, year)
                .le(Semester::getYear, Integer.parseInt(year) + 2)
                .orderByAsc(Semester::getStartTime));
    }

}
