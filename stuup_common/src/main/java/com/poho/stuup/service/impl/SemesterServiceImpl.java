package com.poho.stuup.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.SemesterDTO;
import com.poho.stuup.model.vo.SemesterVO;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.SemesterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    public ResponseModel<List<Semester>> getStudentSemester(Long userId) {
        List<Semester> result = new ArrayList<>();
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ResponseModel.ok(result);
        Student student = studentMapper.getStudentForStudentNO(user.getLoginName());
        if (student == null) return ResponseModel.ok(result);
        Integer gradeId = student.getGradeId();
        Grade grade = gradeMapper.selectByPrimaryKey(gradeId);
        if (grade == null) return ResponseModel.ok(result);
        String year = grade.getYear();
        Config config = configService.selectByPrimaryKey(ConfigKeyEnum.LAST_SEMESTER_START_TIME.getKey());
        if (config == null) return ResponseModel.ok(result);
        Date startTime = DateUtil.parseDate(StrUtil.format("{}-{}", year, config.getConfigValue()));
        Semester semester = baseMapper.selectOne(Wrappers.<Semester>lambdaQuery()
                .select(Semester::getStartTime)
                .eq(Semester::getIsCurrent, WhetherEnum.YES.getValue()));
        if (semester == null) return ResponseModel.ok(result);
        Date endTime = semester.getStartTime();
        List<Semester> semesters = baseMapper.selectList(Wrappers.<Semester>lambdaQuery()
                .select(Semester::getId, Semester::getName, Semester::getIsCurrent)
                .between(Semester::getStartTime, startTime, endTime)
                .orderByAsc(Semester::getStartTime));
        return ResponseModel.ok(semesters);
    }

}
