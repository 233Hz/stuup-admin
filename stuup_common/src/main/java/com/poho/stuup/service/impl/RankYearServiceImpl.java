package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.vo.ClassRankVO;
import com.poho.stuup.model.vo.FacultyRankVO;
import com.poho.stuup.model.vo.MajorRankVO;
import com.poho.stuup.model.vo.YearRankVO;
import com.poho.stuup.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 年度排行榜 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Slf4j
@Service
public class RankYearServiceImpl extends ServiceImpl<RankYearMapper, RankYear> implements RankYearService {

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private IGradeService gradeService;

    @Resource
    private IClassService classService;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private ITeacherService teacherService;

    @Resource
    private IMajorService majorService;

    @Resource
    private MajorMapper majorMapper;

    @Resource
    private IFacultyService facultyService;

    @Resource
    private FacultyMapper facultyMapper;

    @Resource
    private IYearService yearService;

    @Resource
    private YearMapper yearMapper;

    @Resource
    private RecAddScoreMapper recAddScoreMapper;


    @Override
    public List<YearRankVO> getSchoolRank(Long yearId) {
        // 所有学生
        List<Student> students = studentMapper.getAllStudent();

        Map<Integer, Class> classMap = classService.classMap();            // 班级
        Map<Integer, Teacher> teacherMap = teacherService.teacherMap();    // 班主任
        Map<Integer, Grade> gradeMap = gradeService.gradeMap();            // 年级
        Map<Integer, Major> majorMap = majorService.majorMap();            // 专业
        Map<Integer, Faculty> facultyMap = facultyService.facultyMap();    // 系部

        List<RecAddScore> recAddScores = recAddScoreMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getStudentId, RecAddScore::getScore)
                .eq(RecAddScore::getYearId, yearId));
        int recordSize = recAddScores.size();

        // 对学生进行分组求和
        Map<Long, BigDecimal> recordSumScore = recAddScores.stream().collect(Collectors.groupingBy(RecAddScore::getStudentId, Collectors.reducing(BigDecimal.ZERO, RecAddScore::getScore, BigDecimal::add)));

        List<YearRankVO> result = students.stream().map(student -> {
                    Integer studentId = student.getId();
                    YearRankVO rankVO = new YearRankVO();
                    rankVO.setStudentId(studentId);
                    rankVO.setStudentName(student.getName());
                    rankVO.setStudentNo(student.getStudentNo());
                    rankVO.setScore(recordSumScore.getOrDefault(studentId.longValue(), BigDecimal.ZERO));
                    Optional.ofNullable(student.getClassId()).flatMap(classId -> Optional.ofNullable(classMap.get(classId))).ifPresent(_class -> {
                        // 设置班级名称
                        rankVO.setClassName(_class.getName());
                        // 设置班主任
                        Integer teacherId = _class.getTeacherId();
                        Optional.ofNullable(teacherMap.get(teacherId)).ifPresent(teacher -> rankVO.setClassTeacher(teacher.getName()));
                        // 设置系部名称
                        Integer facultyId = _class.getFacultyId();
                        Optional.ofNullable(facultyMap.get(facultyId)).ifPresent(faculty -> rankVO.setFacultyName(faculty.getFacultyName()));
                    });
                    //设置所属年级名称
                    Optional.ofNullable(student.getGradeId()).flatMap(gradeId -> Optional.ofNullable(gradeMap.get(gradeId))).ifPresent(grade -> rankVO.setGradeName(grade.getGradeName()));
                    // 设置专业名称
                    Optional.ofNullable(student.getMajorId()).flatMap(majorId -> Optional.ofNullable(majorMap.get(majorId))).ifPresent(major -> rankVO.setMajorName(major.getMajorName()));
                    return rankVO;
                })
                .collect(Collectors.toList());

        students.clear();
        classMap.clear();
        teacherMap.clear();
        gradeMap.clear();
        majorMap.clear();
        facultyMap.clear();
        recAddScores.clear();
        recordSumScore.clear();

        if (recordSize > 0) {
            // 按总分进行排序（降序）
            result.sort(Comparator.comparing(YearRankVO::getScore).reversed());
        }


        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = result.get(0).getScore();

        for (YearRankVO rankVO : result) {
            if (lastStudentScore.compareTo(rankVO.getScore()) != 0) rank++;
            rankVO.setRank(rank);
            lastStudentScore = rankVO.getScore();
        }

        return result;
    }

    @Override
    public List<ClassRankVO> getClassRank(Long yearId) {
        // 所有班级
        List<Class> classes = classMapper.selectAll();

        Map<Integer, Class> classMap = classes.stream().collect(Collectors.toMap(Class::getId, Function.identity()));   //班级
        Map<Integer, Teacher> teacherMap = teacherService.teacherMap();    // 班主任
        Map<Integer, Grade> gradeMap = gradeService.gradeMap();            // 年级
        Map<Integer, Major> majorMap = majorService.majorMap();            // 专业
        Map<Integer, Faculty> facultyMap = facultyService.facultyMap();    // 系部

        List<ClassRankVO> recScores = recAddScoreMapper.getClassRank(yearId);
        int recordSize = recScores.size();

        // 按班级进行分组求和
        Map<Integer, BigDecimal> recordSumScore = recScores.stream()
                .filter(rank -> rank.getClassId() != null)  // 过滤空值
                .collect(Collectors.groupingBy(ClassRankVO::getClassId, Collectors.reducing(BigDecimal.ZERO, ClassRankVO::getScore, BigDecimal::add)));

        List<ClassRankVO> result = classes.stream().map(_class -> {
                    ClassRankVO rankVO = new ClassRankVO();
                    Integer classId = _class.getId();
                    rankVO.setClassId(classId);
                    rankVO.setScore(recordSumScore.getOrDefault(classId, BigDecimal.ZERO));
                    Optional.ofNullable(classId).flatMap(id -> Optional.ofNullable(classMap.get(id))).ifPresent(bClass -> {
                        // 设置班级名称
                        rankVO.setClassName(bClass.getName());
                        // 设置班主任
                        Optional.ofNullable(bClass.getTeacherId()).flatMap(teacherId -> Optional.ofNullable(teacherMap.get(teacherId))).ifPresent(teacher -> rankVO.setClassTeacher(teacher.getName()));
                        // 设置系部名称
                        Optional.ofNullable(bClass.getFacultyId()).flatMap(facultyId -> Optional.ofNullable(facultyMap.get(facultyId))).ifPresent(faculty -> rankVO.setFacultyName(faculty.getFacultyName()));
                        //设置所属年级名称
                        Optional.ofNullable(bClass.getGradeId()).flatMap(gradeId -> Optional.ofNullable(gradeMap.get(gradeId))).ifPresent(grade -> rankVO.setGradeName(grade.getGradeName()));
                        // 设置专业名称
                        Optional.ofNullable(bClass.getMajorId()).flatMap(majorId -> Optional.ofNullable(majorMap.get(majorId))).ifPresent(major -> rankVO.setMajorName(major.getMajorName()));
                    });
                    return rankVO;
                })
                .collect(Collectors.toList());

        classes.clear();
        classMap.clear();
        teacherMap.clear();
        gradeMap.clear();
        majorMap.clear();
        facultyMap.clear();
        recScores.clear();
        recordSumScore.clear();

        if (recordSize > 0) {
            // 按总分进行排序（降序）
            result.sort(Comparator.comparing(ClassRankVO::getScore).reversed());
        }

        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = result.get(0).getScore();

        for (ClassRankVO rankVO : result) {
            if (lastStudentScore.compareTo(rankVO.getScore()) != 0) rank++;
            rankVO.setRank(rank);
            lastStudentScore = rankVO.getScore();
        }

        return result;
    }

    @Override
    public List<MajorRankVO> getMajorRank(Long yearId) {
        // 所有专业
        List<Major> majors = majorMapper.selectAll();

        Map<Integer, Major> majorMap = majors.stream().collect(Collectors.toMap(Major::getOid, Function.identity()));   // 专业
        Map<Integer, Faculty> facultyMap = facultyService.facultyMap();    // 系部

        List<MajorRankVO> recScores = recAddScoreMapper.getMajorRank(yearId);
        int recordSize = recScores.size();

        // 按专业进行分组求和
        Map<Integer, BigDecimal> recordSumScore = recScores.stream()
                .filter(rank -> rank.getMajorId() != null)  // // 过滤空值
                .collect(Collectors.groupingBy(MajorRankVO::getMajorId, Collectors.reducing(BigDecimal.ZERO, MajorRankVO::getScore, BigDecimal::add)));

        List<MajorRankVO> result = majors.stream().map(major -> {
                    MajorRankVO rankVO = new MajorRankVO();
                    Integer majorId = major.getOid();
                    rankVO.setMajorId(majorId);
                    rankVO.setScore(recordSumScore.getOrDefault(majorId, BigDecimal.ZERO));
                    Optional.ofNullable(majorId).flatMap(id -> Optional.ofNullable(majorMap.get(id))).ifPresent(m -> {
                        // 设置专业名称
                        rankVO.setMajorName(m.getMajorName());
                        // 设置在所属系部
                        Optional.ofNullable(m.getFacultyId()).flatMap(facultyId -> Optional.ofNullable(facultyMap.get(facultyId))).ifPresent(faculty -> rankVO.setFacultyName(faculty.getFacultyName()));
                    });
                    return rankVO;
                })
                .collect(Collectors.toList());

        majors.clear();
        majorMap.clear();
        facultyMap.clear();
        recScores.clear();
        recordSumScore.clear();

        if (recordSize > 0) {
            // 按总分进行排序（降序）
            result.sort(Comparator.comparing(MajorRankVO::getScore).reversed());
        }


        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = result.get(0).getScore();

        for (MajorRankVO rankVO : result) {
            if (lastStudentScore.compareTo(rankVO.getScore()) != 0) rank++;
            rankVO.setRank(rank);
            lastStudentScore = rankVO.getScore();
        }

        return result;
    }

    @Override
    public List<FacultyRankVO> getFacultyRank(Long yearId) {
        // 所有专业
        List<Faculty> faculties = facultyMapper.selectAll();

        Map<Integer, String> facultyMap = faculties.stream().collect(Collectors.toMap(Faculty::getOid, Faculty::getFacultyName));   // 系部
        Map<Integer, Class> classMap = classService.classMap();            // 班级

        List<FacultyRankVO> recScores = recAddScoreMapper.getFacultyRank(yearId);
        int recordSize = recScores.size();

        // 按专业进行分组求和
        Map<Integer, BigDecimal> recordSumScore = recScores.stream().filter(recScore -> {
            Integer classId = recScore.getClassId();
            if (classId == null) return false;
            Class _class = classMap.get(classId);
            if (_class == null) return false;
            Integer facultyId = _class.getFacultyId();
            if (facultyId == null) return false;
            recScore.setFacultyId(facultyId);
            return true;
        }).collect(Collectors.groupingBy(FacultyRankVO::getFacultyId, Collectors.reducing(BigDecimal.ZERO, FacultyRankVO::getScore, BigDecimal::add)));

        List<FacultyRankVO> result = faculties.stream().map(faculty -> {
            FacultyRankVO rankVO = new FacultyRankVO();
            Integer facultyId = faculty.getOid();
            rankVO.setFacultyId(facultyId);
            rankVO.setScore(recordSumScore.getOrDefault(facultyId, BigDecimal.ZERO));
            Optional.ofNullable(facultyId).flatMap(id -> Optional.ofNullable(facultyMap.get(id))).ifPresent(rankVO::setFacultyName);
            return rankVO;
        }).collect(Collectors.toList());

        faculties.clear();
        facultyMap.clear();
        classMap.clear();
        recScores.clear();
        recordSumScore.clear();

        if (recordSize > 0) {
            // 按总分进行排序（降序）
            result.sort(Comparator.comparing(FacultyRankVO::getScore).reversed());
        }


        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = result.get(0).getScore();

        for (FacultyRankVO rankVO : result) {
            if (lastStudentScore.compareTo(rankVO.getScore()) != 0) rank++;
            rankVO.setRank(rank);
            lastStudentScore = rankVO.getScore();
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateRank(Date date) {
        log.info("====================开始统计学年排行榜====================");
        long start = System.currentTimeMillis();
        Year year = yearMapper.findByRange(date);
        if (year == null) return;
        Long yearId = year.getOid();
        // 所有学生
        List<Student> students = studentMapper.getAllStudent();

        // 查询出该学期的所有记录
        List<RecAddScore> recAddScores = recAddScoreMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getStudentId, RecAddScore::getScore)
                .eq(RecAddScore::getState, WhetherEnum.YES.getValue())
                .eq(RecAddScore::getYearId, yearId));
        int recordSize = recAddScores.size();

        // 对学生id进行分组求和
        Map<Long, BigDecimal> recScoresSumScore = recAddScores.stream().collect(Collectors.groupingBy(RecAddScore::getStudentId, Collectors.reducing(BigDecimal.ZERO, RecAddScore::getScore, BigDecimal::add)));

        List<RankYear> rankYearList = students.stream().map(student -> {
                    RankYear rankSemester = new RankYear();
                    rankSemester.setYearId(yearId);
                    rankSemester.setStudentId(student.getId().longValue());
                    rankSemester.setScore(recScoresSumScore.getOrDefault(student.getId().longValue(), BigDecimal.ZERO));
                    return rankSemester;
                })
                .collect(Collectors.toList());

        students.clear();
        recAddScores.clear();
        recScoresSumScore.clear();

        if (recordSize > 0) {
            rankYearList.sort(Comparator.comparing(RankYear::getScore).reversed());
        }

        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = rankYearList.get(0).getScore();

        for (RankYear rankYear : rankYearList) {
            if (lastStudentScore.compareTo(rankYear.getScore()) != 0) rank++;
            rankYear.setRanking(rank);
            lastStudentScore = rankYear.getScore();
            baseMapper.insert(rankYear);
        }

        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }
}
