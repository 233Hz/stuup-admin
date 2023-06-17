package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.AutStateEnum;
import com.poho.stuup.constant.ValidationGroups;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.AudGrow;
import com.poho.stuup.model.User;
import com.poho.stuup.model.dto.GrowRecordDTO;
import com.poho.stuup.model.vo.GrowApplyRecordVO;
import com.poho.stuup.model.vo.GrowAuditRecordVO;
import com.poho.stuup.service.AudGrowService;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * <p>
 * 成长项目审核记录 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/audGrow")
public class AudGrowController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private AudGrowService audGrowService;

    @Resource
    private RecScoreService recScoreService;

    @Resource
    private StudentMapper studentMapper;

    @Resource
    private IUserService userService;

    /**
     * @description: 申请成长项目
     * @param: data
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 16:44
     */
    @PostMapping("/apply")
    public ResponseModel<Boolean> applyGrowItem(@Validated({ValidationGroups.Insert.class}) @RequestBody AudGrow data) {
        String userId = ProjectUtil.obtainLoginUser(request);
        data.setApplicant(Long.parseLong(userId));
        return audGrowService.applyGrowItem(data);
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
        System.out.println(state == AutStateEnum.TO_BE_SUBMITTED.getCode());
        if (AutStateEnum.TO_BE_SUBMITTED.getCode() != state && AutStateEnum.RETURN.getCode() != state)
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
        if (AutStateEnum.TO_BE_SUBMITTED.getCode() != state && AutStateEnum.RETURN.getCode() != state)
            return ResponseModel.failed("当前状态无法删除");
        return ResponseModel.ok(audGrowService.removeById(id), "删除成功");
    }

    /**
     * @description: 查询我的申请记录（分页）
     * @param: page
     * @param: query
     * @return: com.poho.common.custom.ResponseModel<com.baomidou.mybatisplus.core.metadata.IPage < com.poho.stuup.model.vo.GrowApplyRecordVO>>
     * @author BUNGA
     * @date: 2023/6/16 11:08
     */
    @GetMapping("/page/apply")
    public ResponseModel<IPage<GrowApplyRecordVO>> pageGrowApplyRecord(Page<GrowApplyRecordVO> page, GrowRecordDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        query.setUserId(Long.parseLong(userId));
        return ResponseModel.ok(audGrowService.pageGrowApplyRecord(page, query));
    }

    /**
     * @description: 查询我的审核记录（分页）
     * @param: page
     * @param: query
     * @return: com.poho.common.custom.ResponseModel<com.baomidou.mybatisplus.core.metadata.IPage < com.poho.stuup.model.vo.GrowAuditRecordVO>>
     * @author BUNGA
     * @date: 2023/6/15 17:38
     */
    @GetMapping("/page/audit")
    public ResponseModel<IPage<GrowAuditRecordVO>> pageGrowAuditRecord(Page<GrowAuditRecordVO> page, GrowRecordDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        query.setUserId(Long.parseLong(userId));
        return audGrowService.pageGrowAuditRecord(page, query);
    }

    /**
     * @description: 提交审核
     * @param: id
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 16:44
     */
    @GetMapping("/submit/{id}")
    public ResponseModel<Boolean> submitGrowItem(@PathVariable("id") Long id) {
        AudGrow audGrow = audGrowService.getById(id);
        Integer state = audGrow.getState();
        if (AutStateEnum.TO_BE_SUBMITTED.getCode() != state && AutStateEnum.RETURN.getCode() != state)
            return ResponseModel.failed("当前状态无法提交");
        String userId = ProjectUtil.obtainLoginUser(request);
        audGrowService.updateRecordState(id, audGrow.getGrowId(), AutStateEnum.PENDING_REVIEW, Long.parseLong(userId), null);
        return ResponseModel.ok(null, "提交成功");
    }

    /**
     * @description: 通过审核
     * @param: id
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 17:38
     */
    @GetMapping("/pass/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> passGrowItem(@PathVariable("id") Long id) {
        AudGrow audGrow = audGrowService.getById(id);
        User user = userService.selectByPrimaryKey(audGrow.getApplicant());
        Long studentId = studentMapper.findStudentId(user.getLoginName());
        if (studentId == null) return ResponseModel.failed("未查询到申请人信息，请联系管理员");
        Integer state = audGrow.getState();
        if (AutStateEnum.PENDING_REVIEW.getCode() != state)
            return ResponseModel.failed("当前状态无法审核");
        String userId = ProjectUtil.obtainLoginUser(request);
        // 更新状态
        audGrowService.updateRecordState(id, audGrow.getGrowId(), AutStateEnum.PASS, Long.parseLong(userId), null);
        // 计算积分
        recScoreService.calculateStudentScore(audGrow.getGrowId(), studentId);
        return ResponseModel.ok(null, "审核已通过");
    }

    /**
     * @description: 拒绝审核
     * @param: id
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 17:41
     */
    @GetMapping("/refuse/{id}")
    public ResponseModel<Boolean> refuseGrowItem(@PathVariable("id") Long id, @RequestParam(value = "reason", required = false, defaultValue = "审核已拒绝,未填写原因") String reason) {
        AudGrow audGrow = audGrowService.getById(id);
        Integer state = audGrow.getState();
        if (AutStateEnum.PENDING_REVIEW.getCode() != state)
            return ResponseModel.failed("当前状态无法审核");
        String userId = ProjectUtil.obtainLoginUser(request);
        audGrowService.updateRecordState(id, audGrow.getGrowId(), AutStateEnum.REFUSE, Long.parseLong(userId), reason);
        return ResponseModel.ok(null, "审核已拒绝");
    }

    /**
     * @description: 退回审核
     * @param: id
     * @return: com.poho.common.custom.ResponseModel<java.lang.Boolean>
     * @author BUNGA
     * @date: 2023/6/15 17:42
     */
    @GetMapping("/return/{id}")
    public ResponseModel<Boolean> returnGrowItem(@PathVariable("id") Long id, @RequestParam(value = "reason", required = false, defaultValue = "审核已退回,未填写原因") String reason) {
        AudGrow audGrow = audGrowService.getById(id);
        Integer state = audGrow.getState();
        if (AutStateEnum.PENDING_REVIEW.getCode() != state)
            return ResponseModel.failed("当前状态无法审核");
        String userId = ProjectUtil.obtainLoginUser(request);
        audGrowService.updateRecordState(id, audGrow.getGrowId(), AutStateEnum.RETURN, Long.parseLong(userId), reason);
        return ResponseModel.ok(null, "审核已退回");
    }

}
