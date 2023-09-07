package com.poho.stuup.handle.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.constant.MilitaryLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.service.RecMilitaryService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 军事训练记录填报报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecMilitaryListener implements ReadListener<RecMilitaryExcel> {

    private final StudentMapper studentMapper;
    private final RecMilitaryService recMilitaryService;
    private final GrowthItem growthItem;
    private final Long yearId;
    private final Long semesterId;
    private final Long userId;
    private final long batchCode;


    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecMilitaryExcel> recMilitaryExcels = new ArrayList<>();

    public RecMilitaryListener(StudentMapper studentMapper, RecMilitaryService recMilitaryService, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, long batchCode) {
        this.studentMapper = studentMapper;
        this.recMilitaryService = recMilitaryService;
        this.growthItem = growthItem;
        this.yearId = yearId;
        this.semesterId = semesterId;
        this.userId = userId;
        this.batchCode = batchCode;
    }

    @Override
    public void invoke(RecMilitaryExcel data, AnalysisContext context) {
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
        if (StrUtil.isBlank(data.getLevel())) {
            errorMessages.add("等级不能为空");
        }
        if (MilitaryLevelEnum.getValueForLabel(data.getLevel()) == null) {
            errorMessages.add("等级不存在");
        }
        if (StrUtil.isBlank(data.getExcellent())) {
            errorMessages.add("是否优秀不能为空");
        }
        if (WhetherEnum.getValueForLabel(data.getExcellent()) == null) {
            errorMessages.add("请输入是/否");
        }

        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            success++;
            recMilitaryExcels.add(data);
        } else {
            fail++;
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        recMilitaryService.saveRecMilitaryExcel(recMilitaryExcels, growthItem, yearId, semesterId, userId, batchCode);
        log.info("==========导入已完成！==========");
    }
}
