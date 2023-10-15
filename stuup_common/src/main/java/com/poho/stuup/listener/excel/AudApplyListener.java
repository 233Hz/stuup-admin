package com.poho.stuup.listener.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.Student;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.service.AudGrowService;
import com.poho.stuup.service.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class AudApplyListener implements ReadListener<RecDefaultExcel> {

    private final Long yearId;
    private final Long semesterId;
    private final IStudentService studentService;
    private final AudGrowService audGrowService;
    private final GrowthItem growthItem;
    private final Long userId;
    private final StopWatch stopWatch;
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<Long> studentIds = new ArrayList<>();
    public int total;
    public List<ExcelError> errors = new ArrayList<>();

    public AudApplyListener(Long yearId, Long semesterId, IStudentService studentService, AudGrowService audGrowService, GrowthItem growthItem, Long userId, StopWatch stopWatch) {
        this.yearId = yearId;
        this.semesterId = semesterId;
        this.studentService = studentService;
        this.audGrowService = audGrowService;
        this.growthItem = growthItem;
        this.userId = userId;
        this.stopWatch = stopWatch;
    }

    @Override
    public void invoke(RecDefaultExcel data, AnalysisContext context) {
        total++;
        Integer rowIndex = context.readRowHolder().getRowIndex();
        List<String> errorMessages = new ArrayList<>();
        String studentNo = data.getStudentNo();
        if (StrUtil.isBlank(studentNo)) {
            errorMessages.add("学号不能为空");
        }
        if (StrUtil.isBlank(data.getStudentName())) {
            errorMessages.add("姓名不能为空");
        }
        Long studentId = studentMap.get(studentNo);
        if (studentId == null) {
            studentId = studentService.findIdByStudentNo(studentNo);
        }
        if (studentId == null) {
            errorMessages.add("该学生不存在");
        } else {
            studentMap.put(studentNo, studentId);
        }
        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            studentIds.add(studentId);
        } else {
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        stopWatch.stop();
        stopWatch.start("获取所有学生");
        List<Student> allStudent = studentService.getAllStudent();
        stopWatch.stop();
        if (total == 0) return;
        stopWatch.start("插入审核数据");
        audGrowService.batchSubmitAudGrows(allStudent, studentIds, yearId, semesterId, growthItem, userId);
        stopWatch.stop();
    }
}
