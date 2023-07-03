package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.RankMonthMapper;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.vo.ProgressRankVO;
import com.poho.stuup.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 学期排行榜 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Service
public class RankMonthServiceImpl extends ServiceImpl<RankMonthMapper, RankMonth> implements RankMonthService {

    @Resource
    private IGradeService gradeService;

    @Resource
    private IClassService classService;

    @Resource
    private ITeacherService teacherService;

    @Resource
    private IMajorService majorService;

    @Resource
    private IFacultyService facultyService;


    @Override
    public List<ProgressRankVO> getProgressRank() {
        Date date = new Date();
        Date lastMonthDate = DateUtil.offset(date, DateField.MONTH, -1);
        int year = DateUtil.year(lastMonthDate);
        int month = DateUtil.month(lastMonthDate) + 1;
        List<ProgressRankVO> result = baseMapper.getProgressRank(year, month);
        if (CollUtil.isNotEmpty(result)) {
            Map<Integer, Class> classMap = classService.classMap();            // 班级
            Map<Integer, Teacher> teacherMap = teacherService.teacherMap();    // 班主任
            Map<Integer, Grade> gradeMap = gradeService.gradeMap();            // 年级
            Map<Integer, Major> majorMap = majorService.majorMap();            // 专业
            Map<Integer, Faculty> facultyMap = facultyService.facultyMap();    // 系部

            for (ProgressRankVO progressRankVO : result) {
                Optional.ofNullable(progressRankVO.getClassId()).flatMap(classId -> Optional.ofNullable(classMap.get(classId))).ifPresent(_class -> {
                    // 设置班级名称
                    progressRankVO.setClassName(_class.getName());
                    // 设置班主任
                    Integer teacherId = _class.getTeacherId();
                    Optional.ofNullable(teacherMap.get(teacherId)).ifPresent(teacher -> progressRankVO.setClassTeacher(teacher.getName()));
                    // 设置系部名称
                    Integer facultyId = _class.getFacultyId();
                    Optional.ofNullable(facultyMap.get(facultyId)).ifPresent(faculty -> progressRankVO.setFacultyName(faculty.getFacultyName()));
                });

                //设置所属年级名称
                Optional.ofNullable(progressRankVO.getGradeId()).flatMap(gradeId -> Optional.ofNullable(gradeMap.get(gradeId))).ifPresent(grade -> progressRankVO.setGradeName(grade.getGradeName()));
                // 设置专业名称
                Optional.ofNullable(progressRankVO.getMajorId()).flatMap(majorId -> Optional.ofNullable(majorMap.get(majorId))).ifPresent(major -> progressRankVO.setMajorName(major.getMajorName()));
            }

            classMap.clear();
            teacherMap.clear();
            gradeMap.clear();
            majorMap.clear();
            facultyMap.clear();

            // 按照进步名次排序
            result.sort(Comparator.comparing(ProgressRankVO::getRankChange).reversed());

            // 设置排名
            int rank = 1;
            Integer lastStudentRankChange = result.get(0).getRankChange();

            for (ProgressRankVO progressRankVO : result) {
                if (!Objects.equals(lastStudentRankChange, progressRankVO.getRankChange())) rank++;
                progressRankVO.setRank(rank);
                lastStudentRankChange = progressRankVO.getRankChange();
            }
        }
        return result;
    }
}
