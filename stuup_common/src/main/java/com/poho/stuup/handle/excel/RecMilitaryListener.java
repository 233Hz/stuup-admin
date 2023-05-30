package com.poho.stuup.handle.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.constant.MilitaryLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.service.RecMilitaryService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BUNGA
 * @description: 军事训练记录填报报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecMilitaryListener implements ReadListener<RecMilitaryExcel> {

    private final StudentMapper studentMapper;
    private final RecMilitaryService recMilitaryService;
    private final long batchCode;

    public List<ExcelError> errors = new ArrayList<>();

    public int total, success, fail;

    public RecMilitaryListener(StudentMapper studentMapper, RecMilitaryService recMilitaryService, long batchCode) {
        this.studentMapper = studentMapper;
        this.recMilitaryService = recMilitaryService;
        this.batchCode = batchCode;
    }

    @Override
    public void invoke(RecMilitaryExcel data, AnalysisContext context) {
        total++;
        Integer rowIndex = context.readRowHolder().getRowIndex();
        List<String> errorMessages = new ArrayList<>();
        if (StrUtil.isBlank(data.getStudentNo())) {
            errorMessages.add("学号不能为空");
        }
        if (StrUtil.isBlank(data.getStudentName())) {
            errorMessages.add("姓名不能为空");
        }
        Long studentId = studentMapper.findStudentId(data.getStudentNo());
        if (studentId == null) {
            errorMessages.add("该学生不存在");
        } else {
            data.setStudentId(studentId);
        }
        if (StrUtil.isBlank(data.getLevel())) {
            errorMessages.add("等级不能为空");
        }
        if (MilitaryLevelEnum.getLabelValue(data.getLevel()) == null) {
            errorMessages.add("等级不存在");
        }
        if (StrUtil.isBlank(data.getExcellent())) {
            errorMessages.add("是否优秀不能为空");
        }
        if (WhetherEnum.getLabelValue(data.getExcellent()) == null) {
            errorMessages.add("请输入是/否");
        }

        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            data.setBatchCode(batchCode);
            boolean flag = recMilitaryService.saveData(data);
            if (flag) {
                success++;
            } else {
                fail++;
            }
        } else {
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("==========导入已完成！==========");
    }
}
