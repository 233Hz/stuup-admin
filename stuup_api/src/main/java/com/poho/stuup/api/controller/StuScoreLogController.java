package com.poho.stuup.api.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.StudentRecScoreDTO;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.service.StuScoreLogService;
import com.poho.stuup.util.ProjectUtil;
import com.poho.stuup.util.Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 学生积分日志 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@RestController
@RequestMapping("/stuScoreLog")
public class StuScoreLogController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private StuScoreLogService stuScoreLogService;


    @GetMapping("/page")
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
        return stuScoreLogService.pageStudentRecScore(page, Long.parseLong(userId), query);
    }

}
