package com.poho.stuup.event;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.stuup.constant.*;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.StatisticsRankEventDTO;
import com.poho.stuup.model.dto.SystemMagVO;
import com.poho.stuup.service.AnnouncementService;
import com.poho.stuup.service.AnnouncementUserService;
import com.poho.stuup.service.IConfigService;
import com.poho.stuup.service.RecAddScoreService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author BUNGA
 * @description: 事件监听器处理
 * @date 2023/6/12 14:35
 */

@Slf4j
@Component
public class EventListenerHandle {

    @Resource
    private EventPublish eventPublish;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private IConfigService configService;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private RankYearMapper rankYearMapper;

    @Resource
    private RankMonthMapper rankMonthMapper;

    @Resource
    private RankSemesterMapper rankSemesterMapper;

    @Resource
    private AnnouncementService announcementService;

    @Resource
    private AnnouncementUserService announcementUserService;


    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handle(@NotNull StatisticsYearRankEvent event) {
        log.info("====================开始统计学年排行榜====================");
        long start = System.currentTimeMillis();
        StatisticsRankEventDTO statisticsRankEvent = event.getStatisticsRankEvent();
        Long yearId = statisticsRankEvent.getYearId();
        // 所有学生
        List<Student> students = studentMapper.getAllStudent();

        // 查询出该学期的所有记录
        List<RecAddScore> recAddScores = recAddScoreService.list(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getStudentId, RecAddScore::getScore)
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
            rankYearMapper.insert(rankYear);
        }

        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handle(@NotNull StatisticsMonthRankEvent event) {
        log.info("====================开始统计每月排行榜====================");
        long start = System.currentTimeMillis();
        StatisticsRankEventDTO statisticsRankEvent = event.getStatisticsRankEvent();
        Date startTime = statisticsRankEvent.getStartTime();
        // 所有学生
        List<Student> students = studentMapper.getAllStudent();


        int year = DateUtil.year(startTime);
        int month = DateUtil.month(startTime) + 1;
        Date monthBegin = DateUtil.beginOfMonth(startTime);
        Date monthEnd = DateUtil.endOfDay(startTime);

        // 查询改月时间段内的记录
        List<RecAddScore> recAddScores = recAddScoreService.list(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getStudentId, RecAddScore::getScore)
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
        Date offsetTime = DateUtil.offset(startTime, DateField.MONTH, -1);
        int lastYear = DateUtil.year(offsetTime);
        int lastMonth = DateUtil.month(offsetTime) + 1;
        // 上月榜单记录
        List<RankMonth> lastRankMonths = rankMonthMapper.selectList(Wrappers.<RankMonth>lambdaQuery()
                .select(RankMonth::getRank, RankMonth::getStudentId, RankMonth::getScore)
                .eq(RankMonth::getYear, lastYear)
                .eq(RankMonth::getMonth, lastMonth));
        Map<Long, RankMonth> lastRankMonthMap = lastRankMonths.stream().collect(Collectors.toMap(RankMonth::getStudentId, Function.identity()));

        List<User> users = userMapper.getAllUserLoginNamesAndIds();             //用户
        List<Teacher> teachers = teacherMapper.getAllTeacherJobNosAndIds();     //教师
        List<Class> classes = classMapper.getClassTeacherIdsAndIds();           //班级

        Map<Integer, Student> studentMap = students.stream().collect(Collectors.toMap(Student::getId, Function.identity()));            //学生
        Map<String, Long> userLoginNameToIdMap = users.stream().filter(user -> user.getLoginName() != null).collect(Collectors.toMap(User::getLoginName, User::getOid));            //用户
        Map<Integer, String> teacherIdToJobNoMap = teachers.stream().filter(teacher -> teacher.getJobNo() != null).collect(Collectors.toMap(Teacher::getId, Teacher::getJobNo));      //教师
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
//            Random random = new Random();
//            rank = random.nextInt(100);
            //============测试生成随机排名============
            rankMonth.setRank(rank);
            RankMonth value = lastRankMonthMap.get(studentId);
            if (value != null) {
                // 上月排名
                Integer lastRank = value.getRank();
                AtomicReference<Long> studentUserId = new AtomicReference<>();
                AtomicReference<Long> classTeacherUserId = new AtomicReference<>();
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
                            .flatMap(teacherId -> Optional.ofNullable(teacherIdToJobNoMap.get(teacherId)))
                            .flatMap(teacherJobNo -> Optional.ofNullable(userLoginNameToIdMap.get(teacherJobNo)))
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
                            teacherSystemMagVO.setTitle(StrUtil.format("您班{}同学上月进步{}名", studentName, progressRanking));
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
            rankMonthMapper.insert(rankMonth);
        }

        students.clear();
        userLoginNameToIdMap.clear();
        teacherIdToJobNoMap.clear();
        classIdToTeacherIdMap.clear();
        lastRankMonths.clear();
        lastRankMonthMap.clear();

        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handle(@NotNull StatisticsSemesterRankEvent event) {
        log.info("====================开始统计学期排行榜====================");
        long start = System.currentTimeMillis();
        StatisticsRankEventDTO statisticsRankEvent = event.getStatisticsRankEvent();
        Long semesterId = statisticsRankEvent.getSemesterId();
        // 所有学生
        List<Student> students = studentMapper.getAllStudent();

        // 查询出该学期的所有记录
        List<RecAddScore> recAddScores = recAddScoreService.list(Wrappers.<RecAddScore>lambdaQuery()
                .select(RecAddScore::getStudentId, RecAddScore::getScore)
                .eq(RecAddScore::getSemesterId, semesterId));
        int recordSize = recAddScores.size();

        // 对学生id进行分组求和
        Map<Long, BigDecimal> recScoresSumScore = recAddScores.stream().collect(Collectors.groupingBy(RecAddScore::getStudentId, Collectors.reducing(BigDecimal.ZERO, RecAddScore::getScore, BigDecimal::add)));

        List<RankSemester> rankSemesterList = students.stream().map(student -> {
                    RankSemester rankSemester = new RankSemester();
                    rankSemester.setSemesterId(semesterId);
                    rankSemester.setStudentId(student.getId().longValue());
                    rankSemester.setScore(recScoresSumScore.getOrDefault(student.getId().longValue(), BigDecimal.ZERO));
                    return rankSemester;
                })
                .collect(Collectors.toList());

        students.clear();
        recAddScores.clear();
        recScoresSumScore.clear();

        if (recordSize > 0) {
            rankSemesterList.sort(Comparator.comparing(RankSemester::getScore).reversed());
        }

        // 设置排名
        int rank = 1;
        BigDecimal lastStudentScore = rankSemesterList.get(0).getScore();

        for (RankSemester rankSemester : rankSemesterList) {
            if (lastStudentScore.compareTo(rankSemester.getScore()) != 0) rank++;
            rankSemester.setRanking(rank);
            lastStudentScore = rankSemester.getScore();
            rankSemesterMapper.insert(rankSemester);
        }

        long end = System.currentTimeMillis();
        log.info("====================任务执行完毕====================");
        log.info("耗时:{}ms", end - start);
        log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
    }

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void handle(@NotNull SystemMsgEvent event) {
        SystemMagVO systemMagVO = event.getSystemMagVO();
        Announcement announcement = new Announcement();
        announcement.setTitle(systemMagVO.getTitle());
        announcement.setType(AnnouncementTypeEnum.SYSTEM.getValue());
        announcement.setState(AnnouncementStateEnum.PUBLISHED.getValue());
        announcementService.save(announcement);
        AnnouncementUser announcementUser = new AnnouncementUser();
        announcementUser.setAnnouncementId(announcement.getId());
        announcementUser.setUserId(systemMagVO.getUserId());
        announcementUserService.save(announcementUser);
    }

}
