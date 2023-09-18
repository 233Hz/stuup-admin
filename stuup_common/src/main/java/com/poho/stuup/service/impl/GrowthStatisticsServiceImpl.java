package com.poho.stuup.service.impl;

import cn.hutool.core.date.StopWatch;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.CompareEnum;
import com.poho.stuup.constant.RoleEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.CountStudentApplyDTO;
import com.poho.stuup.model.dto.StudentIdAndUserIdDTO;
import com.poho.stuup.model.vo.GrowthStatisticsVO;
import com.poho.stuup.service.GrowthStatisticsService;
import com.poho.stuup.util.Utils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author BUNGA
 * @description
 * @date 2023/9/12 15:53
 */
@Slf4j
@Service
@AllArgsConstructor
public class GrowthStatisticsServiceImpl implements GrowthStatisticsService {

    private final GrowthStatisticsMapper growthStatisticsMapper;
    private final YearMapper yearMapper;
    private final SemesterMapper semesterMapper;
    private final UserMapper userMapper;
    private final GradeMapper gradeMapper;
    private final TeacherMapper teacherMapper;
    private final ClassMapper classMapper;
    private final MajorMapper majorMapper;
    private final RankSemesterMapper rankSemesterMapper;
    private final AudGrowMapper audGrowMapper;
    private final StudentMapper studentMapper;
    private final StuScoreMapper stuScoreMapper;

    @Override
    public List<GrowthStatisticsVO> list(Long userId, Long yearId, Long semesterId) {
        boolean hasRole = Utils.hasRole(userId, RoleEnum.ADMIN, RoleEnum.SCHOOL_LEADERS);
        StopWatch stopWatch = new StopWatch();
        if (!hasRole) {
            User user = userMapper.selectByPrimaryKey(userId);
            if (user != null && user.getUserType() == UserTypeEnum.TEACHER.getValue()) {
                String loginName = userMapper.getLoginNameById(userId);
                if (loginName != null) {
                    Integer teacherId = teacherMapper.getIdByJobNo(loginName);
                    List<Integer> classIds = classMapper.getClassIdFormTeacherId(teacherId);
                    if (classIds != null && !classIds.isEmpty()) {
                        stopWatch.start("查询学期排行榜数据");
                        List<GrowthStatisticsVO> list = growthStatisticsMapper.list(yearId, semesterId, classIds);
                        stopWatch.stop();
                        log.info("========================list========================");
                        log.info(stopWatch.getTotalTimeMillis() + "ms");
                        log.info(stopWatch.prettyPrint());
                        return convertGrowthStatistics(list, yearId, semesterId);
                    }
                }
            }
            return Collections.emptyList();
        }
        stopWatch.start("查询学期排行榜数据");
        List<GrowthStatisticsVO> list = growthStatisticsMapper.list(yearId, semesterId, null);
        stopWatch.stop();
        log.info("========================list========================");
        log.info(stopWatch.getTotalTimeMillis() + "ms");
        log.info(stopWatch.prettyPrint());
        return convertGrowthStatistics(list, yearId, semesterId);
    }

    private List<GrowthStatisticsVO> convertGrowthStatistics(List<GrowthStatisticsVO> list, Long yearId, Long semesterId) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        StopWatch stopWatch = new StopWatch();
        // 学年
        stopWatch.start("查询学年");
        Year year = yearMapper.selectByPrimaryKey(yearId);
        stopWatch.stop();
        // 学期
        stopWatch.start("查询学期");
        Semester semester = semesterMapper.selectById(semesterId);
        stopWatch.stop();
        // 专业
        stopWatch.start("查询专业");
        List<Major> majorList = majorMapper.selectAllIdName();
        Map<Integer, String> majorMap = majorList.stream().collect(Collectors.toMap(Major::getOid, Major::getMajorName));
        majorList.clear();
        stopWatch.stop();
        // 班级
        stopWatch.start("查询班级");
        List<Class> classList = classMapper.selectAllIdNameTeacherId();
        Map<Integer, Class> classMap = classList.stream().collect(Collectors.toMap(Class::getId, Function.identity()));
        classList.clear();
        stopWatch.stop();
        // 教师
        stopWatch.start("查询教师");
        List<Teacher> teacherList = teacherMapper.selectAllIdName();
        Map<Integer, String> teacherMap = teacherList.stream().collect(Collectors.toMap(Teacher::getId, Teacher::getName));
        teacherList.clear();
        stopWatch.stop();
        // 年级
        stopWatch.start("查询年级");
        List<Grade> gradeList = gradeMapper.selectAllIdName();
        Map<Integer, String> gradeMap = gradeList.stream().collect(Collectors.toMap(Grade::getOid, Grade::getGradeName));
        gradeList.clear();
        stopWatch.stop();
        // 上学期成长值
        stopWatch.start("查询上学期成长值");
        Semester lastSemester = semesterMapper.selectLastById(semesterId);
        Map<Long, RankSemester> rankSemesterMap = new HashMap<>();
        if (lastSemester != null) {
            List<RankSemester> rankSemesters = rankSemesterMapper.selectList(Wrappers.<RankSemester>lambdaQuery()
                    .select(RankSemester::getStudentId, RankSemester::getScore)
                    .eq(RankSemester::getSemesterId, lastSemester.getId()));
            rankSemesterMap = rankSemesters.stream().collect(Collectors.toMap(RankSemester::getStudentId, Function.identity()));
            rankSemesters.clear();
        }
        stopWatch.stop();
        // 申请项目数
        stopWatch.start("查询申请项目数");
        HashMap<String, Object> audApplyParams = new HashMap<>();
        audApplyParams.put("yearId", yearId);
        audApplyParams.put("semesterId", semesterId);
        List<CountStudentApplyDTO> countStudentApplyList = audGrowMapper.countStudentApply(audApplyParams);
        Map<Long, Long> countStudentApplyMap = countStudentApplyList.stream().collect(Collectors.toMap(CountStudentApplyDTO::getStudentId, CountStudentApplyDTO::getCount));
        countStudentApplyList.clear();
        List<StudentIdAndUserIdDTO> studentIdAndUserIdList = studentMapper.selectAllStudentIdUserId();
        Map<Long, Long> studentIdAndUserIdMap = studentIdAndUserIdList.stream().collect(Collectors.toMap(StudentIdAndUserIdDTO::getUserId, StudentIdAndUserIdDTO::getStudentId));
        studentIdAndUserIdList.clear();
        stopWatch.stop();
        // 总积分
        stopWatch.start("查询总积分");
        List<StuScore> stuScoreList = stuScoreMapper.selectList(Wrappers.<StuScore>lambdaQuery()
                .select(StuScore::getStudentId, StuScore::getScore));
        Map<Long, BigDecimal> stuScoreMap = stuScoreList.stream().collect(Collectors.toMap(StuScore::getStudentId, StuScore::getScore));
        stuScoreList.clear();
        stopWatch.stop();

        stopWatch.start("转换处理");
        for (GrowthStatisticsVO growthStatistics : list) {
            // 学年
            growthStatistics.setYearName(year.getYearName());
            // 学期
            growthStatistics.setSemesterName(semester.getName());
            // 专业
            Optional.ofNullable(growthStatistics.getMajorId())
                    .map(majorMap::get)
                    .ifPresent(growthStatistics::setMajorName);
            // 班级
            Optional.ofNullable(growthStatistics.getClassId())
                    .map(classMap::get)
                    .ifPresent(_class -> {
                        growthStatistics.setClassName(_class.getName());
                        growthStatistics.setTeacherId(_class.getTeacherId());
                    });
            // 班主任
            Optional.ofNullable(growthStatistics.getTeacherId())
                    .map(teacherMap::get)
                    .ifPresent(growthStatistics::setClassTeacher);
            // 年级
            Optional.ofNullable(growthStatistics.getGradeId())
                    .map(gradeMap::get)
                    .ifPresent(growthStatistics::setGradeName);
            Long studentId = growthStatistics.getStudentId();
            // 与上学期对比
            if (lastSemester != null) {
                Optional.ofNullable(rankSemesterMap.get(studentId))
                        .flatMap(lastSemesterRank -> Optional.ofNullable(lastSemesterRank.getScore()))
                        .ifPresent(lastSemesterScore -> {
                            BigDecimal scoreCompare = growthStatistics.getSemesterScore().subtract(lastSemesterScore);
                            CompareEnum compare;
                            if (scoreCompare.compareTo(BigDecimal.ZERO) >= 0) {
                                compare = CompareEnum.UP;
                            } else {
                                compare = CompareEnum.DOWN;
                            }
                            growthStatistics.setLastSemesterCompare(compare.getValue());
                            growthStatistics.setCompareResult(scoreCompare.abs());
                        });
            }
            // 申请项目数
            Optional.ofNullable(studentIdAndUserIdMap.get(studentId))
                    .map(countStudentApplyMap::get)
                    .ifPresent(growthStatistics::setApplyCount);
            // 总积分
            Optional.ofNullable(stuScoreMap.get(studentId))
                    .ifPresent(growthStatistics::setTotalScore);

        }
        stopWatch.stop();
        log.info("========================convertGrowthStatistics========================");
        log.info(stopWatch.getTotalTimeMillis() + "ms");
        log.info(stopWatch.prettyPrint());
        return list;
    }
}
