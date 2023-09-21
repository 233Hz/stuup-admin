package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.UserTypeEnum;
import com.poho.stuup.model.StuScore;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.User;
import com.poho.stuup.service.IStudentService;
import com.poho.stuup.service.IUserService;
import com.poho.stuup.service.StuScoreService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * <p>
 * 学生积分表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@RestController
@RequestMapping("/stuScore")
public class StuScoreController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private IUserService userService;

    @Resource
    private IStudentService studentService;

    @Resource
    private StuScoreService stuScoreService;

    @GetMapping("/getStudentScore")
    public ResponseModel<BigDecimal> getStudentScore() {
        String userId = ProjectUtil.obtainLoginUserId(request);
        User user = userService.selectByPrimaryKey(Long.valueOf(userId));
        Integer userType = user.getUserType();
        if (userType != UserTypeEnum.STUDENT.getValue()) return ResponseModel.ok();
        Student student = studentService.getStudentForStudentNO(user.getLoginName());
        if (student == null) return ResponseModel.ok();
        StuScore stuScore = stuScoreService.getOne(Wrappers.<StuScore>lambdaQuery().select(StuScore::getScore).eq(StuScore::getStudentId, student.getId()));
        if (stuScore == null) return ResponseModel.ok();
        return ResponseModel.ok(Optional.ofNullable(stuScore.getScore()).orElse(BigDecimal.ZERO));
    }

}
