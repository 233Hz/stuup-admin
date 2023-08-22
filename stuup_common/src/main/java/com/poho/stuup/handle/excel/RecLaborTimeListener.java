package com.poho.stuup.handle.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.service.RecLaborTimeService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 生产劳动实践记录填报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecLaborTimeListener implements ReadListener<RecLaborTimeExcel> {

    private final StudentMapper studentMapper;
    private final RecLaborTimeService recLaborTimeService;
    private final GrowthItem growthItem;
    private final Long yearId;
    private final Long semesterId;
    private final Long userId;
    private final long batchCode;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecLaborTimeExcel> recLaborTimeExcels = new ArrayList<>();

    public RecLaborTimeListener(StudentMapper studentMapper, RecLaborTimeService recLaborTimeService, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, long batchCode) {
        this.studentMapper = studentMapper;
        this.recLaborTimeService = recLaborTimeService;
        this.growthItem = growthItem;
        this.yearId = yearId;
        this.semesterId = semesterId;
        this.userId = userId;
        this.batchCode = batchCode;
    }

    @Override
    public void invoke(RecLaborTimeExcel data, AnalysisContext context) {
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
        if (StrUtil.isBlank(data.getHours())) {
            errorMessages.add("累计课时不能为空");
        }
        if (Utils.isNumber(data.getHours())) {
            errorMessages.add("累计课时必须为数字");
        }

        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            success++;
            recLaborTimeExcels.add(data);
        } else {
            fail++;
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        recLaborTimeService.saveRecLaborTimeExcel(recLaborTimeExcels, growthItem, yearId, semesterId, userId, batchCode);
        log.info("==========导入已完成！==========");
    }
}
