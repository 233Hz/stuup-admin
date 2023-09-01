package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.vo.StudentRecScoreVO;
import com.poho.stuup.model.vo.StudentScoreDetailsVO;
import com.poho.stuup.service.StuScoreLogService;
import com.poho.stuup.util.ProjectUtil;
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
    public ResponseModel<StudentScoreDetailsVO> studentScoreDetailsPage(Page<StudentRecScoreVO> page) {
        String userId = ProjectUtil.obtainLoginUser(request);
        return stuScoreLogService.studentScoreDetailsPage(page, Long.parseLong(userId));
    }

}
