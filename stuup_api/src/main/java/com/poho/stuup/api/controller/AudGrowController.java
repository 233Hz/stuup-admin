package com.poho.stuup.api.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.*;
import com.poho.stuup.event.EventPublish;
import com.poho.stuup.event.SystemMsgEvent;
import com.poho.stuup.handle.excel.AudApplyListener;
import com.poho.stuup.model.AudGrow;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.GrowRecordDTO;
import com.poho.stuup.model.dto.GrowthAuditDTO;
import com.poho.stuup.model.dto.SystemMagVO;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.model.vo.AudGrowthVO;
import com.poho.stuup.service.*;
import com.poho.stuup.util.ProjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 成长项目审核记录 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Slf4j
@RestController
@RequestMapping("/audGrow")
public class AudGrowController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private IYearService yearService;

    @Resource
    private SemesterService semesterService;

    @Resource
    private AudGrowService audGrowService;

    @Resource
    private RecAddScoreService recAddScoreService;

    @Resource
    private IStudentService studentService;

    @Resource
    private IUserService userService;

    @Resource
    private GrowthItemService growthItemService;

    @Resource
    private EventPublish eventPublish;

    /**
     * @description: 申请成长项目
     * @param: data
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 16:44
     */
    @PostMapping("/apply")
    public ResponseModel<Boolean> apply(@Validated({ValidationGroups.Insert.class}) @RequestBody AudGrow data) {
        String userId = ProjectUtil.obtainLoginUser(request);
        data.setApplicant(Long.parseLong(userId));
        return audGrowService.apply(data);
    }

    /**
     * @description: 更新记录
     * @param: data
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 16:44
     */
    @PostMapping("/update")
    public ResponseModel<Boolean> updateAudGrow(@Validated({ValidationGroups.Update.class}) @RequestBody AudGrow data) {
        AudGrow audGrow = audGrowService.getById(data.getId());
        Integer state = audGrow.getState();
        if (AudStateEnum.TO_BE_SUBMITTED.getCode() != state && AudStateEnum.NO_PASS.getCode() != state)
            return ResponseModel.failed("当前状态无法修改");
        if (!Objects.equals(data.getGrowId(), audGrow.getGrowId())) return ResponseModel.failed("无法修改申请的项目");
        return ResponseModel.ok(audGrowService.updateById(data), "修改成功");
    }

    /**
     * @description: 删除记录
     * @param: id
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 16:44
     */
    @DeleteMapping("/delete/{id}")
    public ResponseModel<Boolean> deleteAudGrow(@PathVariable("id") Long id) {
        AudGrow audGrow = audGrowService.getById(id);
        Integer state = audGrow.getState();
        if (AudStateEnum.TO_BE_SUBMITTED.getCode() != state && AudStateEnum.NO_PASS.getCode() != state)
            return ResponseModel.failed("当前状态无法删除");
        return ResponseModel.ok(audGrowService.removeById(id), "删除成功");
    }

    /**
     * 分页查询本人提交记录（本人提交）
     */
    @GetMapping("/page/student/apply")
    public ResponseModel<IPage<AudGrowthVO>> pageStudentApply(Page<AudGrowthVO> page, GrowRecordDTO query) {
        if (query.getYearId() == null || query.getSemesterId() == null)
            return ResponseModel.failed("请选择要查询的学年和学期");
        Long userId = Long.parseLong(ProjectUtil.obtainLoginUser(request));
        query.setType(ApplyGrowTypeEnum.SELF.getValue());
        query.setApplicantId(userId);
        query.setSubmitterId(userId);
        query.setSortOrder("desc");
        return ResponseModel.ok(audGrowService.pageAud(page, query));
    }

    /**
     * 分页查询本人提交记录（学生会提交）
     */
    @GetMapping("/page/studentUnion/apply")
    public ResponseModel<IPage<AudGrowthVO>> pageStudentUnionApply(Page<AudGrowthVO> page, GrowRecordDTO query) {
        if (query.getYearId() == null || query.getSemesterId() == null)
            return ResponseModel.failed("请选择要查询的学年和学期");
        Long userId = Long.parseLong(ProjectUtil.obtainLoginUser(request));
        query.setType(ApplyGrowTypeEnum.OTHERS.getValue());
        query.setSubmitterId(userId);
        query.setSortOrder("desc");
        return ResponseModel.ok(audGrowService.pageAud(page, query));
    }

    /**
     * 分页查询我的审核记录（学生提交）
     */
    @GetMapping("/page/student/audit")
    public ResponseModel<IPage<AudGrowthVO>> pageStudentAudit(Page<AudGrowthVO> page, GrowRecordDTO query) {
        if (query.getYearId() == null || query.getSemesterId() == null)
            return ResponseModel.failed("请选择要查询的学年和学期");
        Long userId = Long.parseLong(ProjectUtil.obtainLoginUser(request));
        query.setType(ApplyGrowTypeEnum.SELF.getValue());
        query.setAuditorId(userId);
        query.setState(AudStateEnum.PENDING_REVIEW.getCode());
        return ResponseModel.ok(audGrowService.pageAud(page, query));
    }

    /**
     * 分页查询我的审核记录（学生会提交提交）
     */
    @GetMapping("/page/studentUnion/audit")
    public ResponseModel<IPage<AudGrowthVO>> pageStudentUnionAudit(Page<AudGrowthVO> page, GrowRecordDTO query) {
        if (query.getYearId() == null || query.getSemesterId() == null)
            return ResponseModel.failed("请选择要查询的学年和学期");
        Long userId = Long.parseLong(ProjectUtil.obtainLoginUser(request));
        query.setType(ApplyGrowTypeEnum.OTHERS.getValue());
        query.setAuditorId(userId);
        query.setState(AudStateEnum.PENDING_REVIEW.getCode());
        return ResponseModel.ok(audGrowService.pageAud(page, query));
    }

    /**
     * 查询所有审核记录
     */
    @GetMapping("/page")
    public ResponseModel<IPage<AudGrowthVO>> pageAudit(Page<AudGrowthVO> page, GrowRecordDTO query) {
        if (query.getYearId() == null || query.getSemesterId() == null)
            return ResponseModel.failed("请选择要查询的学年和学期");
        query.setSortOrder("desc");
        return ResponseModel.ok(audGrowService.pageAud(page, query));
    }

    /**
     * 提交审核
     */
    @GetMapping("/submit/{id}")
    public ResponseModel<Boolean> submit(@PathVariable("id") Long id) {
        AudGrow audGrow = audGrowService.getById(id);
        Integer state = audGrow.getState();
        if (AudStateEnum.TO_BE_SUBMITTED.getCode() != state && AudStateEnum.NO_PASS.getCode() != state)
            return ResponseModel.failed("当前状态无法提交");
        String userId = ProjectUtil.obtainLoginUser(request);
        audGrowService.updateState(id, audGrow.getGrowId(), AudStateEnum.PENDING_REVIEW, Long.parseLong(userId), null);
        // 发布通知
        User user = userService.selectByPrimaryKey(audGrow.getApplicant());
        SystemMagVO systemMagVO = new SystemMagVO();
        systemMagVO.setTitle(StrUtil.format("您有新的成长项目审核，您班 {} 提交了申请", user.getUserName()));
        systemMagVO.setUserId(audGrow.getAuditor());
        eventPublish.publishEvent(new SystemMsgEvent(systemMagVO));
        return ResponseModel.ok(null, "提交成功");
    }

    /**
     * 审核通过
     */
    @PostMapping("/pass")
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> pass(@Validated({ValidationGroups.Audit.Single.Pass.class}) @RequestBody GrowthAuditDTO data) {
        Long id = data.getId();
        AudGrow audGrow = audGrowService.getById(id);
        User user = userService.selectByPrimaryKey(audGrow.getApplicant());
        Long studentId = studentService.findIdByStudentNo(user.getLoginName());
        if (studentId == null) return ResponseModel.failed("未查询到申请人信息，请联系管理员");
        Integer state = audGrow.getState();
        if (AudStateEnum.PENDING_REVIEW.getCode() != state)
            return ResponseModel.failed("当前状态无法审核");
        String userId = ProjectUtil.obtainLoginUser(request);
        // 更新状态
        audGrowService.updateState(id, audGrow.getGrowId(), AudStateEnum.PASS, Long.parseLong(userId), null);
        // 计算积分
        recAddScoreService.calculateStudentScore(audGrow.getGrowId(), studentId);
        // 发布通知
        Long growId = audGrow.getGrowId();
        GrowthItem growthItem = growthItemService.getById(growId);
        SystemMagVO systemMagVO = new SystemMagVO();
        systemMagVO.setTitle(StrUtil.format("您申请的\"{}\"项目已通过了审核", growthItem.getName()));
        systemMagVO.setUserId(audGrow.getApplicant());
        eventPublish.publishEvent(new SystemMsgEvent(systemMagVO));
        return ResponseModel.ok(null, "审核已通过");
    }


    /**
     * 审核不通过
     */
    @PostMapping("/noPass")
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> onPass(@Validated({ValidationGroups.Audit.Single.NoPass.class}) @RequestBody GrowthAuditDTO data) {
        Long id = data.getId();
        String reason = data.getReason();
        AudGrow audGrow = audGrowService.getById(id);
        Integer state = audGrow.getState();
        if (AudStateEnum.PENDING_REVIEW.getCode() != state)
            return ResponseModel.failed("当前状态无法审核");
        String userId = ProjectUtil.obtainLoginUser(request);
        // 更新状态
        audGrowService.updateState(id, audGrow.getGrowId(), AudStateEnum.NO_PASS, Long.parseLong(userId), reason);
        // 发布通知
        Long growId = audGrow.getGrowId();
        GrowthItem growthItem = growthItemService.getById(growId);
        SystemMagVO systemMagVO = new SystemMagVO();
        systemMagVO.setTitle(StrUtil.format("您申请的\"{}\"项目审核未通过", growthItem.getName()));
        systemMagVO.setUserId(audGrow.getApplicant());
        eventPublish.publishEvent(new SystemMsgEvent(systemMagVO));
        return ResponseModel.ok(null, "审核已退回");
    }

    /**
     * 批量通过审核
     *
     * @param data
     * @return
     */
    @PostMapping("/batchPass")
    public ResponseModel<Boolean> batchPass(@Validated({ValidationGroups.Audit.Batch.Pass.class}) @RequestBody GrowthAuditDTO data) {
        String userId = ProjectUtil.obtainLoginUser(request);
        List<Long> ids = data.getIds();
        int success = 0;
        for (Long id : ids) {
            if (audGrowService.pass(id, Long.valueOf(userId))) success++;
        }
        return ResponseModel.ok(null, StrUtil.format("审核成功，总条数：{}条，成功数：{}条", ids.size(), success));
    }

    @PostMapping("/batchNoPass")
    public ResponseModel<Boolean> batchNoPass(@Validated({ValidationGroups.Audit.Batch.NoPass.class}) @RequestBody GrowthAuditDTO data) {
        String userId = ProjectUtil.obtainLoginUser(request);
        List<Long> ids = data.getIds();
        String reason = data.getReason();
        int success = 0;
        for (Long id : ids) {
            if (audGrowService.noPass(id, reason, Long.valueOf(userId))) success++;
        }
        return ResponseModel.ok(null, StrUtil.format("审核成功，总条数：{}条，成功数：{}条", ids.size(), success));
    }

    /**
     * 导入审核记录
     */
    @PostMapping("/apply/import")
    public ResponseModel<List<ExcelError>> importApply(MultipartFile file, @RequestParam String recCode) {
        String userId = ProjectUtil.obtainLoginUser(request);
        GrowthItem growthItem = growthItemService.getOne(Wrappers.<GrowthItem>lambdaQuery()
                .eq(GrowthItem::getCode, recCode));
        if (growthItem == null) return ResponseModel.failed("导入的项目不存在");
        Integer gatherer = growthItem.getGatherer();
        if (gatherer != GrowGathererEnum.STUDENT_UNION.getValue())
            return ResponseModel.failed("当前项目采集类型无法从该入口导入");
        Long yearId = yearService.getCurrentYearId();
        if (yearId == null) return ResponseModel.failed("不在当前学年时间范围内");
        Long semesterId = semesterService.getCurrentSemesterId();
        if (semesterId == null) return ResponseModel.failed("不在当前学期时间范围内");
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start("读取excel");
            AudApplyListener audApplyListener = new AudApplyListener(yearId, semesterId, studentService, audGrowService, growthItem, Long.parseLong(userId), stopWatch);
            EasyExcel.read(file.getInputStream(), RecDefaultExcel.class, audApplyListener).sheet().doRead();
            if (audApplyListener.total == 0) {
                return ResponseModel.failed("Excel为空！");
            }
            if (CollUtil.isNotEmpty(audApplyListener.errors)) {
                return ResponseModel.ok(audApplyListener.errors);
            }
            Integer calculateType = growthItem.getCalculateType();
            String message = null;
            if (calculateType == CalculateTypeEnum.PLUS.getValue()) {
                message = StrUtil.format("导入成功,导入为{},[加分：{}条]", CalculateTypeEnum.PLUS.getType(), audApplyListener.total);
            } else if (calculateType == CalculateTypeEnum.MINUS.getValue()) {
                Integer total = studentService.countAtSchoolNum();
                int plusTotal = total - audApplyListener.total;
                log.info(stopWatch.shortSummary());
                log.info(stopWatch.prettyPrint());
                message = StrUtil.format("导入成功,导入为{},[总条数：{}条，加分：{}条，扣分：{}条]", CalculateTypeEnum.MINUS.getType(), total, plusTotal, audApplyListener.total);
            }
            return ResponseModel.ok(null, message);
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }

}
