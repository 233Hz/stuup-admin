package com.poho.stuup.api.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.dto.SemesterDTO;
import com.poho.stuup.model.vo.SemesterVO;
import com.poho.stuup.service.IYearService;
import com.poho.stuup.service.SemesterService;
import com.poho.stuup.util.ProjectUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 学期管理表 前端控制器
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-27
 */
@RestController
@RequestMapping("/semester")
public class SemesterController {

    @Resource
    private HttpServletRequest request;

    @Resource
    private IYearService yearService;

    @Resource
    private SemesterService semesterService;

    @GetMapping("/page")
    public ResponseModel<IPage<SemesterVO>> pageSemester(Page<SemesterVO> page, SemesterDTO query) {
        return ResponseModel.ok(semesterService.pageSemester(page, query));
    }

    @PostMapping("/save_update")
    public ResponseModel<Boolean> saveOrUpdateSemester(@Valid @RequestBody Semester semester) {
        Long yearId = semester.getYearId();
        Date startTime = semester.getStartTime();
        Date endTime = semester.getEndTime();
        if (startTime.after(endTime)) return ResponseModel.failed("开始时间不能在结束时间之后");
        Year year = yearService.selectByPrimaryKey(yearId);
        if (year == null) return ResponseModel.failed("设置的学年不存在");
        Date yearStart = year.getYearStart();
        Date yearEnd = year.getYearEnd();
        if (!DateUtil.isIn(startTime, yearStart, yearEnd))
            return ResponseModel.failed("开始时间不在该学年起止时间范围内");
        if (!DateUtil.isIn(endTime, yearStart, yearEnd))
            return ResponseModel.failed("结束时间不在该学年起止时间范围内");
        String userId = ProjectUtil.obtainLoginUser(request);
        semester.setCreateUser(Long.valueOf(userId));
        String message = semester.getId() == null ? "添加" : "修改";
        return ResponseModel.ok(semesterService.saveOrUpdate(semester), StrUtil.format("{}成功", message));
    }

    @DeleteMapping("/{id}")
    public ResponseModel<Boolean> delSemester(@PathVariable("id") Long id) {
        return ResponseModel.ok(semesterService.removeById(id), "删除成功");
    }

    @GetMapping("/setCurrent/{id}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseModel<Boolean> setCurrentSemester(@PathVariable("id") Long id) {
        semesterService.update(Wrappers.<Semester>lambdaUpdate()
                .set(Semester::getIsCurrent, WhetherEnum.NO.getValue())
                .ne(Semester::getId, id));
        return ResponseModel.ok(semesterService.update(Wrappers.<Semester>lambdaUpdate().set(Semester::getIsCurrent, WhetherEnum.YES.getValue()).eq(Semester::getId, id)), "设置成功");
    }

    @GetMapping("/getStudentSemester")
    public ResponseModel<List<Semester>> getStudentSemester() {
        String userId = ProjectUtil.obtainLoginUser(request);
        return semesterService.getStudentSemester(Long.valueOf(userId));
    }
}
