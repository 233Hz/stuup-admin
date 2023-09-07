package com.poho.stuup.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.ApplyGrowTypeEnum;
import com.poho.stuup.constant.AudStateEnum;
import com.poho.stuup.constant.CalculateTypeEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.event.EventPublish;
import com.poho.stuup.event.SystemMsgEvent;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.GrowRecordDTO;
import com.poho.stuup.model.dto.SystemMagVO;
import com.poho.stuup.model.vo.AudGrowthVO;
import com.poho.stuup.service.AudGrowService;
import com.poho.stuup.service.RecAddScoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * 成长项目审核记录 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Slf4j
@Service
public class AudGrowServiceImpl extends ServiceImpl<AudGrowMapper, AudGrow> implements AudGrowService {

    @Resource
    private YearMapper yearMapper;

    @Resource
    private SemesterMapper semesterMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private AudLogMapper audLogMapper;

    @Resource
    private GrowthMapper growthMapper;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private GrowthItemMapper growthItemMapper;

    @Resource
    private EventPublish eventPublish;

    @Override
    public IPage<AudGrowthVO> pageAud(Page<AudGrowthVO> page, GrowRecordDTO query) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("mapper查询");
        IPage<AudGrowthVO> pageResult = baseMapper.pageAud(page, query);
        stopWatch.stop();
        List<AudGrowthVO> pageRecords = pageResult.getRecords();
        if (!pageRecords.isEmpty()) {
            stopWatch.start("处理名称");
            List<Growth> growths = growthMapper.selectList(Wrappers.<Growth>lambdaQuery()
                    .select(Growth::getId, Growth::getName));
            Map<Long, String> growthIdForNameMap = growths.stream().collect(Collectors.toMap(Growth::getId, Growth::getName));
            growths.clear();
            List<Long> auditorIds = new ArrayList<>();
            List<Long> submitterId = new ArrayList<>();
            List<Integer> classIds = new ArrayList<>();
            List<Long> yearIds = new ArrayList<>();
            List<Long> semesterIds = new ArrayList<>();
            for (AudGrowthVO pageRecord : pageRecords) {
                Optional.ofNullable(pageRecord.getL1Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(pageRecord::setL1Name);
                Optional.ofNullable(pageRecord.getL2Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(pageRecord::setL2Name);
                Optional.ofNullable(pageRecord.getL3Id())
                        .map(growthIdForNameMap::get)
                        .ifPresent(pageRecord::setL3Name);
                auditorIds.add(pageRecord.getAuditorId());
                submitterId.add(pageRecord.getSubmitterId());
                Optional.ofNullable(pageRecord.getClassId())
                        .filter(classId -> !classIds.contains(classId))
                        .ifPresent(classIds::add);
                Optional.ofNullable(pageRecord.getYearId())
                        .filter(yearId -> !yearIds.contains(yearId))
                        .ifPresent(yearIds::add);
                Optional.ofNullable(pageRecord.getSemesterId())
                        .filter(semesterId -> !semesterIds.contains(semesterId))
                        .ifPresent(semesterIds::add);
            }
            growthIdForNameMap.clear();
            Map<Long, String> yearIdForNameMap = new HashMap<>();
            Map<Long, String> semesterIdForNameMap = new HashMap<>();
            Map<Integer, String> classIdForNameMap = new HashMap<>();
            List<Long> mergedIds = Stream.of(auditorIds.stream(), submitterId.stream())
                    .flatMap(Function.identity())
                    .distinct()
                    .collect(Collectors.toList());
            auditorIds.clear();
            submitterId.clear();
            List<User> users = userMapper.getUserNameByIds(mergedIds);
            Map<Long, String> userIdForNameMap = users.stream().collect(Collectors.toMap(User::getOid, User::getUserName));
            users.clear();
            if (!yearIds.isEmpty()) {
                List<Year> yearList = yearMapper.getYearNameByIds(yearIds);
                yearIdForNameMap = yearList.stream().collect(Collectors.toMap(Year::getOid, Year::getYearName));
                yearIds.clear();
                yearList.clear();
            }
            if (!semesterIds.isEmpty()) {
                List<Semester> semesterList = semesterMapper.getSemesterNameByIds(semesterIds);
                semesterIdForNameMap = semesterList.stream().collect(Collectors.toMap(Semester::getId, Semester::getName));
                semesterIds.clear();
                semesterList.clear();
            }
            if (!classIds.isEmpty()) {
                List<Class> classList = classMapper.getClassNameByIds(classIds);
                classIdForNameMap = classList.stream().collect(Collectors.toMap(Class::getId, Class::getName));
                classIds.clear();
                classList.clear();
            }
            for (AudGrowthVO pageRecord : pageRecords) {
                // 设置审核人
                Optional.ofNullable(pageRecord.getAuditorId())
                        .map(userIdForNameMap::get)
                        .ifPresent(pageRecord::setAuditorName);
                // 设置提交人
                Optional.ofNullable(pageRecord.getSubmitterId())
                        .map(userIdForNameMap::get)
                        .ifPresent(pageRecord::setSubmitterName);
                // 设置学年名称
                Optional.ofNullable(pageRecord.getYearId())
                        .map(yearIdForNameMap::get)
                        .ifPresent(pageRecord::setYearName);
                // 设置学期名称
                Optional.ofNullable(pageRecord.getSemesterId())
                        .map(semesterIdForNameMap::get)
                        .ifPresent(pageRecord::setSemesterName);
                // 设置班级名称
                Optional.ofNullable(pageRecord.getClassId())
                        .map(classIdForNameMap::get)
                        .ifPresent(pageRecord::setClassName);
            }
            users.clear();
            yearIdForNameMap.clear();
            semesterIdForNameMap.clear();
            classIdForNameMap.clear();
            stopWatch.stop();
            log.info(stopWatch.getTotalTimeMillis() + "ms");
            log.info(stopWatch.prettyPrint());
        }
        return pageResult;
    }


    @Override
    public ResponseModel<Boolean> apply(AudGrow data) {
        Long applicant = data.getApplicant();
        User studentUser = userMapper.selectByPrimaryKey(applicant);
        Integer userType = studentUser.getUserType();
        if (UserTypeEnum.STUDENT.getValue() != userType) return ResponseModel.failed("您不是学生，无法申请");
        String loginName = studentUser.getLoginName();
        Student student = studentMapper.getStudentForStudentNO(loginName);
        if (student == null) return ResponseModel.failed("未查询到您的学生信息，请联系管理员");
        Integer classId = student.getClassId();
        Integer classTeacherId = classMapper.getTeacherIdNoByClassId(classId);
        if (classTeacherId == null) return ResponseModel.failed("您所在的班级未设置班主任，请联系管理员");
        String teacherJobNo = teacherMapper.getJobNoById(classTeacherId);
        if (teacherJobNo == null) return ResponseModel.failed("您所在的班级未查询到该班主任信息，请联系管理员");
        Long teacherUserId = userMapper.getIdByLoginName(teacherJobNo);
        if (teacherUserId == null) return ResponseModel.failed("您所在的班级班主任信息有误，请联系管理员");
        Long yearId = yearMapper.getCurrentYearId();
        if (yearId == null) return ResponseModel.failed("不在当前学年时间范围内");
        Long semesterId = semesterMapper.getCurrentSemesterId();
        if (semesterId == null) return ResponseModel.failed("不在当前学期时间范围内");
        data.setYearId(yearId);
        data.setSemesterId(semesterId);
        data.setType(ApplyGrowTypeEnum.SELF.getValue());
        data.setAuditor(teacherUserId);
        data.setSubmitter(studentUser.getOid());
        this.save(data);
        return ResponseModel.ok(true, "申请成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateState(Long id, Long growId, AudStateEnum audStateEnum, Long userId, String reason) {
        this.update(Wrappers.<AudGrow>lambdaUpdate()
                .set(AudGrow::getState, audStateEnum.getCode())
                .eq(AudGrow::getId, id));
        AudLog audLog = new AudLog();
        audLog.setAudId(id);
        audLog.setGrowId(growId);
        audLog.setUserId(userId);
        audLog.setState(audStateEnum.getCode());
        audLog.setReason(reason);
        audLogMapper.insert(audLog);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSubmitAudGrows(List<Student> allStudent, List<Long> importStudentIds, Long yearId, Long semesterId, GrowthItem growthItem, Long userId) {
        Integer calculateType = growthItem.getCalculateType();
        Map<Integer, Long> classIdForTeacherMap = new HashMap<>();
        if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
            for (Long studentId : importStudentIds) {
                Student student = studentMapper.selectByPrimaryKey(studentId.intValue());
                if (student == null) continue;
                String studentNo = student.getStudentNo();
                if (studentNo == null) continue;
                Long studentUserId = userMapper.getIdByLoginName(studentNo);
                Integer classId = student.getClassId();
                Long teacherUserId = classIdForTeacherMap.get(classId);
                if (teacherUserId == null) {
                    Integer teacherId = classMapper.getTeacherIdNoByClassId(classId);
                    if (teacherId == null) continue;
                    String teacherJobNo = teacherMapper.getJobNoById(teacherId);
                    if (teacherJobNo == null) continue;
                    teacherUserId = userMapper.getIdByLoginName(teacherJobNo);
                    if (teacherUserId == null) continue;
                    classIdForTeacherMap.put(classId, teacherUserId);
                }
                AudGrow audGrow = new AudGrow();
                audGrow.setGrowId(growthItem.getId());
                audGrow.setYearId(yearId);
                audGrow.setSemesterId(semesterId);
                audGrow.setType(ApplyGrowTypeEnum.OTHERS.getValue());
                audGrow.setApplicant(studentUserId);
                audGrow.setAuditor(teacherUserId);
                audGrow.setSubmitter(userId);
                audGrow.setState(AudStateEnum.PENDING_REVIEW.getCode());
                baseMapper.insert(audGrow);
                AudLog audLog = new AudLog();
                audLog.setAudId(audGrow.getId());
                audLog.setGrowId(growthItem.getId());
                audLog.setUserId(userId);
                audLog.setState(AudStateEnum.PENDING_REVIEW.getCode());
                audLogMapper.insert(audLog);
            }
        } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
            for (Student student : allStudent) {
                Integer studentId = student.getId();
                if (importStudentIds.contains(studentId.longValue())) continue;
                String studentNo = student.getStudentNo();
                Long studentUserId = userMapper.getIdByLoginName(studentNo);
                Integer classId = student.getClassId();
                Long teacherUserId = classIdForTeacherMap.get(classId);
                if (teacherUserId == null) {
                    Integer teacherId = classMapper.getTeacherIdNoByClassId(classId);
                    if (teacherId == null) continue;
                    String teacherJobNo = teacherMapper.getJobNoById(teacherId);
                    if (teacherJobNo == null) continue;
                    teacherUserId = userMapper.getIdByLoginName(teacherJobNo);
                    if (teacherUserId == null) continue;
                    classIdForTeacherMap.put(classId, teacherUserId);
                }
                AudGrow audGrow = new AudGrow();
                audGrow.setGrowId(growthItem.getId());
                audGrow.setYearId(yearId);
                audGrow.setSemesterId(semesterId);
                audGrow.setType(ApplyGrowTypeEnum.OTHERS.getValue());
                audGrow.setApplicant(studentUserId);
                audGrow.setAuditor(teacherUserId);
                audGrow.setSubmitter(userId);
                audGrow.setState(AudStateEnum.PENDING_REVIEW.getCode());
                baseMapper.insert(audGrow);
                AudLog audLog = new AudLog();
                audLog.setAudId(audGrow.getId());
                audLog.setGrowId(growthItem.getId());
                audLog.setUserId(userId);
                audLog.setState(AudStateEnum.TO_BE_SUBMITTED.getCode());
                audLogMapper.insert(audLog);
            }
        } else {
            throw new RuntimeException("项目积分刷新周期设置错误，请联系管理员");
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pass(Long id, Long currentUserId) {
        AudGrow audGrow = baseMapper.selectById(id);
        if (audGrow == null) return false;
        String loginName = userMapper.getLoginNameById(audGrow.getApplicant());
        if (loginName == null) return false;
        Long studentId = studentMapper.getIdByStudentNo(loginName);
        if (studentId == null) return false;
        Integer state = audGrow.getState();
        if (AudStateEnum.PENDING_REVIEW.getCode() != state) return false;
        int line = baseMapper.updateState(id, AudStateEnum.PASS.getCode());
        if (line != 1) return false;
        AudLog audLog = new AudLog();
        audLog.setAudId(id);
        audLog.setGrowId(audGrow.getGrowId());
        audLog.setUserId(currentUserId);
        audLog.setState(AudStateEnum.PASS.getCode());
        audLogMapper.insert(audLog);
        recAddScoreService.calculateStudentScore(audGrow.getGrowId(), studentId);
        // 发布通知
        Long growId = audGrow.getGrowId();
        GrowthItem growthItem = growthItemMapper.selectById(growId);
        SystemMagVO systemMagVO = new SystemMagVO();
        systemMagVO.setTitle(StrUtil.format("您申请的\"{}\"项目已通过了审核", growthItem.getName()));
        systemMagVO.setUserId(audGrow.getApplicant());
        eventPublish.publishEvent(new SystemMsgEvent(systemMagVO));
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean noPass(Long id, String reason, Long currentUserId) {
        AudGrow audGrow = baseMapper.selectById(id);
        if (audGrow == null) return false;
        Integer state = audGrow.getState();
        if (AudStateEnum.PENDING_REVIEW.getCode() != state) return false;
        int line = baseMapper.updateState(id, AudStateEnum.NO_PASS.getCode());
        if (line != 1) return false;
        AudLog audLog = new AudLog();
        audLog.setAudId(id);
        audLog.setGrowId(audGrow.getGrowId());
        audLog.setUserId(currentUserId);
        audLog.setState(AudStateEnum.PASS.getCode());
        audLog.setReason(reason);
        audLogMapper.insert(audLog);
        // 发布通知
        Long growId = audGrow.getGrowId();
        GrowthItem growthItem = growthItemMapper.selectById(growId);
        SystemMagVO systemMagVO = new SystemMagVO();
        systemMagVO.setTitle(StrUtil.format("您申请的\"{}\"项目审核未通过", growthItem.getName()));
        systemMagVO.setUserId(audGrow.getApplicant());
        eventPublish.publishEvent(new SystemMsgEvent(systemMagVO));
        return true;
    }

}
