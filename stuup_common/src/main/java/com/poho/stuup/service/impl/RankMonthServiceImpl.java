package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.constant.ChangeTypeEnum;
import com.poho.stuup.constant.CommonConstants;
import com.poho.stuup.constant.ConfigKeyEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.event.EventPublish;
import com.poho.stuup.event.SystemMsgEvent;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.SystemMagVO;
import com.poho.stuup.model.vo.ProgressRankVO;
import com.poho.stuup.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 学期排行榜 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-12
 */
@Slf4j
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

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private IConfigService configService;

    @Resource
    private RecAddScoreMapper recAddScoreMapper;

    @Resource
    private EventPublish eventPublish;


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

            for (ProgressRankVO progressRankVO : result) {
                Optional.ofNullable(progressRankVO.getClassId()).flatMap(classId -> Optional.ofNullable(classMap.get(classId))).ifPresent(_class -> {
                    // 设置班级名称
                    progressRankVO.setClassName(_class.getName());
                    // 设置班主任
                    Integer teacherId = _class.getTeacherId();
                    Optional.ofNullable(teacherMap.get(teacherId)).ifPresent(teacher -> progressRankVO.setClassTeacher(teacher.getName()));
                });
            }

            classMap.clear();
            teacherMap.clear();

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateRank(Date date) {
        log.info("====================开始统计每月排行榜====================");
        long start = System.currentTimeMillis();


        // 所有学生
        List<Student> students = studentMapper.getAllStudent();


        int year = DateUtil.year(date);
        int month = DateUtil.month(date) + 1;
        Date monthBegin = DateUtil.beginOfMonth(date);
        Date monthEnd = DateUtil.endOfDay(date);

        // 查询改月时间段内的记录
        List<RecAddScore> recAddScores = recAddScoreMapper.selectList(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getStudentId, RecAddScore::getScore)
                .eq(RecAddScore::getState, WhetherEnum.YES.getValue())
                .between(RecAddScore::getCreateTime, monthBegin, monthEnd));
        int recordSize = recAddScores.size();

        // 对学生id进行分组求和
        Map<Long, BigDecimal> recScoresSumScore = recAddScores.stream().collect(Collectors.groupingBy(RecAddScore::getStudentId, Collectors.reducing(BigDecimal.ZERO, RecAddScore::getScore, BigDecimal::add)));

        List<RankMonth> rankMonthList = students.stream().map(student -> {
                    Long studentId = Long.valueOf(student.getId());
                    RankMonth rankMonth = new RankMonth();
                    rankMonth.setYear(year);
                    rankMonth.setMonth(month);
                    rankMonth.setStudentId(studentId);
                    rankMonth.setScore(recScoresSumScore.getOrDefault(student.getId().longValue(), BigDecimal.ZERO));
                    return rankMonth;
                })
                .collect(Collectors.toList());

        recAddScores.clear();
        recScoresSumScore.clear();

        if (recordSize > 0) {
            rankMonthList.sort(Comparator.comparing(RankMonth::getScore).reversed());
        }

        // 查询上月榜单
        Date offsetTime = DateUtil.offset(date, DateField.MONTH, -1);
        int lastYear = DateUtil.year(offsetTime);
        int lastMonth = DateUtil.month(offsetTime) + 1;
        // 上月榜单记录
        List<RankMonth> lastRankMonths = baseMapper.selectList(Wrappers.<RankMonth>lambdaQuery()
                .select(RankMonth::getRank, RankMonth::getStudentId, RankMonth::getScore)
                .eq(RankMonth::getYear, lastYear)
                .eq(RankMonth::getMonth, lastMonth));
        Map<Long, RankMonth> lastRankMonthMap = lastRankMonths.stream().collect(Collectors.toMap(RankMonth::getStudentId, Function.identity()));

        List<User> users = userMapper.getAllUserLoginNamesAndIds();             //用户
        List<Teacher> teachers = teacherMapper.getAllTeacherJobNoAndName();     //教师
        List<Class> classes = classMapper.getClassTeacherIdsAndIds();           //班级

        Map<Integer, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, Function.identity()));            //学生
        Map<String, Long> userLoginNameToIdMap = users.stream().filter(user -> user.getLoginName() != null).collect(Collectors.toMap(User::getLoginName, User::getOid));            //用户
        Map<Integer, Teacher> teacherMap = teachers.stream().filter(teacher -> teacher.getJobNo() != null).collect(Collectors.toMap(Teacher::getId, Function.identity()));      //教师
        Map<Integer, Integer> classIdToTeacherIdMap = classes.stream().filter(_class -> _class.getTeacherId() != null).collect(Collectors.toMap(Class::getId, Class::getTeacherId));    //班级

        students.clear();
        users.clear();
        teachers.clear();
        classes.clear();

        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = rankMonthList.get(0).getScore();


        for (RankMonth rankMonth : rankMonthList) {
            Long studentId = rankMonth.getStudentId();
            if (lastStudentScore.compareTo(rankMonth.getScore()) != 0) rank++;
            lastStudentScore = rankMonth.getScore();
            //============测试生成随机排名============
            Random random = new Random();
            rank = random.nextInt(100);
            //============测试生成随机排名============
            rankMonth.setRank(rank);
            RankMonth value = lastRankMonthMap.get(studentId);
            if (value != null) {
                // 上月排名
                Integer lastRank = value.getRank();
                AtomicReference<Long> studentUserId = new AtomicReference<>();
                AtomicReference<Long> classTeacherUserId = new AtomicReference<>();
                AtomicReference<String> classTeacherName = new AtomicReference<>();
                String studentName = null;
                Student student = studentMap.get(studentId.intValue());
                if (student != null) {
                    studentName = student.getName();
                    // 获取学生userId
                    Optional.ofNullable(student.getStudentNo())
                            .flatMap(studentNo -> Optional.ofNullable(userLoginNameToIdMap.get(studentNo)))
                            .ifPresent(studentUserId::set);
                    // 获取该学生班主任userId
                    Optional.of(student.getClassId())
                            .flatMap(classId -> Optional.ofNullable(classIdToTeacherIdMap.get(classId)))
                            .flatMap(teacherId -> Optional.ofNullable(teacherMap.get(teacherId)))
                            .flatMap(teacher -> {
                                classTeacherName.set(teacher.getName());
                                return Optional.ofNullable(userLoginNameToIdMap.get(teacher.getJobNo()));
                            })
                            .ifPresent(classTeacherUserId::set);
                }
                // 设置排名相关形象
                if (rank == lastRank) {
                    rankMonth.setRankTrend(ChangeTypeEnum.SAME.getValue());
                    rankMonth.setRankChange(0);
                } else if (rank > lastRank) {
                    int progressRanking = rank - lastRank; // 进步名次
                    rankMonth.setRankTrend(ChangeTypeEnum.UP.getValue());
                    rankMonth.setRankChange(progressRanking);
                    // 发送进步提醒个班主任
                    int ranking = CommonConstants.DEFAULT_NOTIFY_RANKING; // 获取不到默认10名
                    Config config = configService.selectByPrimaryKey(ConfigKeyEnum.PROGRESS_NOTIFY_RANKING.getKey());
                    if (config != null) ranking = Integer.parseInt(config.getConfigValue());
                    if (progressRanking >= ranking) {
                        // 发布通知
                        if (classTeacherUserId.get() != null) {
                            SystemMagVO teacherSystemMagVO = new SystemMagVO();
                            teacherSystemMagVO.setTitle(StrUtil.format("{}老师，恭喜您，本班同学{}上月进步{}名", classTeacherName, studentName, progressRanking));
                            teacherSystemMagVO.setUserId(classTeacherUserId.get());
                            eventPublish.publishEvent(new SystemMsgEvent(teacherSystemMagVO));
                        }
                        if (studentUserId.get() != null) {
                            SystemMagVO studentSystemMagVO = new SystemMagVO();
                            studentSystemMagVO.setTitle(StrUtil.format("您上月进步了{}名,位于进步榜第{}名", progressRanking, rank));
                            studentSystemMagVO.setUserId(studentUserId.get());
                            eventPublish.publishEvent(new SystemMsgEvent(studentSystemMagVO));
                        }
                    }
                } else {
                    int retrogress = lastRank - rank;  // 退步名次
                    rankMonth.setRankTrend(ChangeTypeEnum.DOWN.getValue());
                    rankMonth.setRankChange(retrogress);

                    // 发送退步提醒个学生个人和班主任
                    int ranking = CommonConstants.DEFAULT_NOTIFY_RANKING; // 获取不到默认10名
                    Config config = configService.selectByPrimaryKey(ConfigKeyEnum.RETROGRESS_NOTIFY_RANKING.getKey());
                    if (config != null) ranking = Integer.parseInt(config.getConfigValue());
                    if (retrogress >= ranking) {
                        // 发布通知
                        if (classTeacherUserId.get() != null) {
                            SystemMagVO teacherSystemMagVO = new SystemMagVO();
                            teacherSystemMagVO.setTitle(StrUtil.format("您班{}同学上月退步较大,退步{}名", studentName, retrogress));
                            teacherSystemMagVO.setUserId(classTeacherUserId.get());
                            eventPublish.publishEvent(new SystemMsgEvent(teacherSystemMagVO));
                        }
                        if (studentUserId.get() != null) {
                            SystemMagVO studentSystemMagVO = new SystemMagVO();
                            studentSystemMagVO.setTitle(StrUtil.format("您上月退步了{}名,退步较大", retrogress));
                            studentSystemMagVO.setUserId(studentUserId.get());
                            eventPublish.publishEvent(new SystemMsgEvent(studentSystemMagVO));
                        }
                    }
                }
                // 设置分数变化
                BigDecimal score = value.getScore();
                rankMonth.setScoreChange(rankMonth.getScore().subtract(score));
            }
            baseMapper.insert(rankMonth);
        }

        students.clear();
        userLoginNameToIdMap.clear();
        teacherMap.clear();
        classIdToTeacherIdMap.clear();
        lastRankMonths.clear();
        lastRankMonthMap.clear();

        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }
}
