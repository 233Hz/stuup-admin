package com.poho.stuup.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AutStateEnum;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.dao.*;
import com.poho.stuup.model.Class;
import com.poho.stuup.model.*;
import com.poho.stuup.model.dto.GrowRecordDTO;
import com.poho.stuup.model.vo.GrowApplyRecordVO;
import com.poho.stuup.model.vo.GrowAuditRecordVO;
import com.poho.stuup.service.AudGrowService;
import com.poho.stuup.service.AudLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 成长项目审核记录 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Service
public class AudGrowServiceImpl extends ServiceImpl<AudGrowMapper, AudGrow> implements AudGrowService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private ClassMapper classMapper;

    @Resource
    private TeacherMapper teacherMapper;

    @Resource
    private AudLogService audLogService;

    @Override
    public ResponseModel<Boolean> applyGrowItem(AudGrow data) {
        Long applicant = data.getApplicant();
        User studentUser = userMapper.selectByPrimaryKey(applicant);
        Integer userType = studentUser.getUserType();
        if (UserTypeEnum.STUDENT.getValue() != userType) return ResponseModel.failed("您不是学生，无法申请");
        String loginName = studentUser.getLoginName();
        Student student = studentMapper.getStudentForStudentNO(loginName);
        if (student == null) return ResponseModel.failed("未查询到您的学生信息，请联系管理员");
        Integer classId = student.getClassId();
        Class _class = classMapper.selectByPrimaryKey(classId);
        Integer teacherId = _class.getTeacherId();
        if (teacherId == null) return ResponseModel.failed("您所在的班级未设置班主任，请联系管理员");
        Teacher teacher = teacherMapper.selectByPrimaryKey(teacherId);
        if (teacher == null) return ResponseModel.failed("您所在的班级未查询到该班主任信息，请联系管理员");
        String teacherNo = teacher.getJobNo();
        User teacherUser = userMapper.checkUser(MapUtil.of("loginName", teacherNo));
        if (teacherUser == null) return ResponseModel.failed("您所在的班级班主任信息有误，请联系管理员");
        data.setAuditor(teacherUser.getOid());
        this.save(data);
        return ResponseModel.ok(true, "申请成功");
    }

    @Override
    public IPage<GrowApplyRecordVO> pageGrowApplyRecord(Page<GrowApplyRecordVO> page, GrowRecordDTO query) {
        return baseMapper.pageGrowApplyRecord(page, query);
    }

    @Override
    public ResponseModel<IPage<GrowAuditRecordVO>> pageGrowAuditRecord(Page<GrowAuditRecordVO> page, GrowRecordDTO query) {
        Long userId = query.getUserId();
        User user = userMapper.selectByPrimaryKey(userId);
        if (user == null) return ResponseModel.failed("您的用户信息不存在，请联系管理员");
        Integer userType = user.getUserType();
        if (UserTypeEnum.TEACHER.getValue() != userType) return ResponseModel.failed("您不是老师，无法参与审核");
        String loginName = user.getLoginName();
        Teacher teacher = teacherMapper.findTeacherByJobNo(loginName);
        if (teacher == null) return ResponseModel.failed("未查询到您的教师信息，请联系管理员");
        Integer teacherId = teacher.getId();
        List<Class> classList = classMapper.findClassByAdmin(teacherId);
        if (CollUtil.isEmpty(classList)) return ResponseModel.failed("您不是班主任，无法参与审核");
        List<Integer> classIds = classList.stream().map(Class::getId).collect(Collectors.toList());
        List<Long> userIds = studentMapper.findStudentUserIdByClassId(classIds);
        if (CollUtil.isEmpty(userIds)) return ResponseModel.failed("您所管理的班级还未有学生");
        IPage<GrowAuditRecordVO> ipage = baseMapper.pageGrowAuditRecord(page, userIds, query);
        return ResponseModel.ok(ipage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRecordState(Long id, Long growId, AutStateEnum autStateEnum, Long userId, String reason) {
        this.update(Wrappers.<AudGrow>lambdaUpdate()
                .set(AudGrow::getState, autStateEnum.getCode())
                .eq(AudGrow::getId, id));
        AudLog audLog = new AudLog();
        audLog.setAudId(id);
        audLog.setGrowId(growId);
        audLog.setUserId(userId);
        audLog.setState(autStateEnum.getCode());
        audLog.setReason(reason);
        audLogService.save(audLog);
    }

}
