package com.poho.stuup.handle.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.service.RecLaborTimeService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BUNGA
 * @description: 生产劳动实践记录填报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecLaborTimeListener implements ReadListener<RecLaborTimeExcel> {

    private final StudentMapper studentMapper;
    private final RecLaborTimeService recLaborTimeService;
    private final long batchCode;

    public List<ExcelError> errors = new ArrayList<>();

    public int total, success, fail;

    public RecLaborTimeListener(StudentMapper studentMapper, RecLaborTimeService recLaborTimeService, long batchCode) {
        this.studentMapper = studentMapper;
        this.recLaborTimeService = recLaborTimeService;
        this.batchCode = batchCode;
    }

    @Override
    public void invoke(RecLaborTimeExcel data, AnalysisContext context) {
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
        if (StrUtil.isBlank(data.getHours())) {
            errorMessages.add("累计课时不能为空");
        }
        if (!Utils.isNumber(data.getHours())) {
            errorMessages.add("累计课时必须为数字");
        }

        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            data.setBatchCode(batchCode);
            boolean flag = recLaborTimeService.saveData(data);
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
