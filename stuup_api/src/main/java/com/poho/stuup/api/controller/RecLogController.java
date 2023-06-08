package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.RecLogDTO;
import com.poho.stuup.model.vo.RecLogDetailsVO;
import com.poho.stuup.model.vo.RecLogVO;
import com.poho.stuup.service.RecLogService;
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
 * 项目记录日志 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-01
 */
@RestController
@RequestMapping("/recLog")
public class RecLogController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private RecLogService recLogService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecLogVO>> getRecLogPage(Page<RecLogVO> page, RecLogDTO query) {
        String userId = ProjectUtil.obtainLoginUser(request);
        query.setUserId(Long.valueOf(userId));
        return ResponseModel.ok(recLogService.getRecLogPage(page, query));
    }

    @GetMapping("/details/{id}")
    public ResponseModel<List<RecLogDetailsVO>> getRecLogDetails(@PathVariable("id") Long id) {
        return ResponseModel.ok(recLogService.getRecLogDetails(id));
    }

}
