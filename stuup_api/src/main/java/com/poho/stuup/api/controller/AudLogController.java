package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.AudLogDTO;
import com.poho.stuup.model.vo.AudLogVO;
import com.poho.stuup.model.vo.AuditLogVO;
import com.poho.stuup.service.AudLogService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 审核日志表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/audLog")
public class AudLogController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private AudLogService audLogService;

    /**
     * @description: 查询当前用户审核操作日志
     * @param: page
     * @param: query
     * @return: com.poho.common.custom.ResponseModel<com.baomidou.mybatisplus.core.metadata.IPage < com.poho.stuup.model.vo.AudLogVO>>
     * @author BUNGA
     * @date: 2023/6/15 18:25
     */
    @GetMapping("/page")
    public ResponseModel<IPage<AudLogVO>> getAuditRecordLog(Page<AudLogVO> page, AudLogDTO query) {
        String userId = ProjectUtil.obtainLoginUserId(request);
        query.setUserId(Long.parseLong(userId));
        return ResponseModel.ok(audLogService.getAuditRecordLog(page, query));
    }

    /**
     * @description: 查记录的审核日志
     * @param: audId
     * @return: com.poho.common.custom.ResponseModel<java.util.List < com.poho.stuup.model.vo.AuditLogVO>>
     * @author BUNGA
     * @date: 2023/6/15 18:28
     */
    @GetMapping("/audit/log/{audId}")
    public ResponseModel<List<AuditLogVO>> getAuditLog(@PathVariable("audId") Long audId) {
        return ResponseModel.ok(audLogService.getAuditLog(audId));
    }

}
