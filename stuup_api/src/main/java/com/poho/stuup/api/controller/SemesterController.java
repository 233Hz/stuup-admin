package com.poho.stuup.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.dto.SemesterDTO;
import com.poho.stuup.model.vo.SemesterVO;
import com.poho.stuup.service.SemesterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private SemesterService semesterService;

    @GetMapping("/page")
    public ResponseModel<IPage<SemesterVO>> pageSemester(Page<SemesterVO> page, SemesterDTO query) {
        return ResponseModel.ok(semesterService.pageSemester(page, query));
    }

    @GetMapping("/list")
    public ResponseModel<List<Map<String, Object>>> getSemesterList() {
        List<Semester> semesterList = semesterService.list(Wrappers.<Semester>lambdaQuery()
                .select(Semester::getId, Semester::getName));
        return ResponseModel.ok(semesterList.stream().map(semester -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", semester.getId());
            map.put("name", semester.getName());
            return map;
        }).collect(Collectors.toList()));
    }
}
