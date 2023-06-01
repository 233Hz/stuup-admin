package com.poho.stuup.handle.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecDefaultExcel;
import com.poho.stuup.service.RecDefaultService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 默认导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecDefaultListener implements ReadListener<RecDefaultExcel> {

    private final long batchCode;
    private final Map<String, Object> params;
    private final GrowthItem growthItem;
    private final StudentMapper studentMapper;
    private final RecDefaultService recDefaultService;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecDefaultExcel> recDefaultExcels = new ArrayList<>();

    public RecDefaultListener(long batchCode, Map<String, Object> params, GrowthItem growthItem, StudentMapper studentMapper, RecDefaultService recDefaultService) {
        this.batchCode = batchCode;
        this.params = params;
        this.growthItem = growthItem;
        this.studentMapper = studentMapper;
        this.recDefaultService = recDefaultService;
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
            studentId = studentMapper.findStudentId(studentNo);
        }
        if (studentId == null) {
            errorMessages.add("该学生不存在");
        } else {
            studentMap.put(studentNo, studentId);
            data.setStudentId(studentId);
        }
        if (CollUtil.isEmpty(errorMessages)) {
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
        recDefaultService.saveRecDefaultExcel(batchCode, growthItem, recDefaultExcels, params);
        log.info("==========导入已完成！==========");
    }
}
