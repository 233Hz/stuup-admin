package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.model.RecScore;
import com.poho.stuup.model.dto.RecScoreDTO;
import com.poho.stuup.model.dto.StudentRecScoreDTO;
import com.poho.stuup.model.vo.RecScoreVO;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.service.RecScoreService;
import com.poho.stuup.util.ProjectUtil;
import com.poho.stuup.util.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 积分记录表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/recScore")
public class RecScoreController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private RecScoreService recScoreService;

    @GetMapping("/page")
    public ResponseModel<IPage<RecScoreVO>> getRecScorePage(Page<RecScoreVO> page, RecScoreDTO query) {
        if (StrUtil.isNotBlank(query.getStartTime())) {
            boolean isDateTime = Utils.isDateTime(query.getStartTime());
            if (!isDateTime) return ResponseModel.failed("开始时间格式错误");
        }
        if (StrUtil.isNotBlank(query.getEndTime())) {
            boolean isDateTime = Utils.isDateTime(query.getEndTime());
            if (!isDateTime) return ResponseModel.failed("结束时间格式错误");
        }
        return ResponseModel.ok(recScoreService.getRecScorePage(page, query));
    }

    @GetMapping("/student/page")
    public ResponseModel<IPage<StudentRecScoreVO>> pageStudentRecScore(Page<StudentRecScoreVO> page, StudentRecScoreDTO query) {
        if (StrUtil.isNotBlank(query.getStartTime())) {
            boolean isDateTime = Utils.isDateTime(query.getStartTime());
            if (!isDateTime) return ResponseModel.failed("开始时间格式错误");
        }
        if (StrUtil.isNotBlank(query.getEndTime())) {
            boolean isDateTime = Utils.isDateTime(query.getEndTime());
            if (!isDateTime) return ResponseModel.failed("结束时间格式错误");
        }
        String userId = ProjectUtil.obtainLoginUser(request);
        return recScoreService.pageStudentRecScore(page, Long.parseLong(userId), query);
    }

    @GetMapping("/updateState")
    public ResponseModel updateRecordState(@RequestParam("idStr") String idStr) {
        List<Long> ids = Arrays.stream(idStr.split(",")).map(Long::valueOf).collect(Collectors.toList());
        recScoreService.update(Wrappers.<RecScore>lambdaUpdate()
                .set(RecScore::getState, WhetherEnum.YES.getValue())
                .in(RecScore::getId, ids));
        return ResponseModel.ok();
    }

}
