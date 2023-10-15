package com.poho.stuup.listener.excel;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.service.RecDefaultService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认导入
 */
@Slf4j
public class RecDefaultListener implements ReadListener<RecDefaultExcel> {

    private final StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);

    private final RecDefaultService recDefaultService = SpringContextHolder.getBean(RecDefaultService.class);

    private final RecImportParams params;
    private final StopWatch stopWatch;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecDefaultExcel> recDefaultExcels = new ArrayList<>();

    public RecDefaultListener(RecImportParams params, StopWatch stopWatch) {
        this.params = params;
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
            studentId = studentMapper.getIdByStudentNo(studentNo);
        }
        if (studentId == null) {
            errorMessages.add("该学生不存在");
        } else {
            studentMap.put(studentNo, studentId);
            data.setStudentId(studentId);
        }
        if (errorMessages.isEmpty()) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            success++;
            recDefaultExcels.add(data);
        } else {
            fail++;
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        stopWatch.stop();
        stopWatch.start("保存数据");
        recDefaultService.saveRecDefaultExcel(recDefaultExcels, params);
        log.info("==========导入已完成！==========");
    }
}
