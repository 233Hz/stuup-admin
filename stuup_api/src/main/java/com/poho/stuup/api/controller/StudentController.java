package com.poho.stuup.api.controller;

import com.poho.common.constant.CommonConstants;
import com.poho.common.custom.ResponseModel;
import com.poho.common.util.MicrovanUtil;
import com.poho.stuup.model.Semester;
import com.poho.stuup.service.IStudentService;
import com.poho.stuup.service.SemesterService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 学生信息维护
 * 提供对学生信息的新增、修改、查询、删除等功能
 */

@Api(tags = "学生信息相关接口")
@RestController
@RequestMapping("/student")
public class StudentController {

    private final static Logger logger = LoggerFactory.getLogger(StudentController.class);
    @Autowired
    private IStudentService studentService;

    @Resource
    private SemesterService semesterService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ResponseModel studentList(String gradeId, String majorId, String key, String current, String size) {
        int page = 1;
        if (MicrovanUtil.isNotEmpty(current)) {
            page = Integer.valueOf(current);
        }
        int pageSize = CommonConstants.PAGE_SIZE;
        if (MicrovanUtil.isNotEmpty(size)) {
            pageSize = Integer.parseInt(size);
        }
        return studentService.findDataPageResult(gradeId, majorId, key, page, pageSize);

    }

    @GetMapping("/semesters")
    public ResponseModel<List<Semester>> studentSemester(@RequestParam("studentId") Integer studentId) {
        return ResponseModel.ok(semesterService.getStudentSemester(studentId));
    }

}
