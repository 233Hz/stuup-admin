package com.poho.stuup.handle.excel;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.service.RecCaucusService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 参加党团学习项目记录填报导入
 * @date 2023/5/25 13:47
 */
@Slf4j
public class RecCaucusListener implements ReadListener<RecCaucusExcel> {

    private final StudentMapper studentMapper;
    private final RecCaucusService recCaucusService;
    private final GrowthItem growthItem;
    private final Long yearId;
    private final Long semesterId;
    private final Long userId;
    private final long batchCode;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecCaucusExcel> recCaucusExcels = new ArrayList<>();

    public RecCaucusListener(StudentMapper studentMapper, RecCaucusService recCaucusService, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, long batchCode) {
        this.studentMapper = studentMapper;
        this.recCaucusService = recCaucusService;
        this.growthItem = growthItem;
        this.yearId = yearId;
        this.semesterId = semesterId;
        this.userId = userId;
        this.batchCode = batchCode;
    }

    @Override
    public void invoke(RecCaucusExcel data, AnalysisContext context) {
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
        if (StrUtil.isBlank(data.getName())) {
            errorMessages.add("项目名称不能为空");
        }
        if (StrUtil.isBlank(data.getLevel())) {
            errorMessages.add("项目级别不能为空");
        }
        if (RecLevelEnum.getValueForLabel(data.getLevel()) == null) {
            errorMessages.add("级别不存在");
        }
        if (StrUtil.isBlank(data.getStartTime())) {
            errorMessages.add("开始时间不能为空");
        }
        if (!Utils.isDate(data.getStartTime())) {
            errorMessages.add("开始时间格式不正确");
        }
        if (StrUtil.isBlank(data.getEndTime())) {
            errorMessages.add("结束时间不能为空");
        }
        if (!Utils.isDate(data.getEndTime())) {
            errorMessages.add("结束时间格式不正确");
        }
        if (StrUtil.isBlank(data.getRole())) {
            errorMessages.add("角色不能为空");
        }
        if (RecRoleEnum.getValueForRole(data.getRole()) == null) {
            errorMessages.add("角色不存在");
        }

        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            success++;
            recCaucusExcels.add(data);
        } else {
            fail++;
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        recCaucusService.saveRecCaucusExcel(recCaucusExcels, growthItem, yearId, semesterId, userId, batchCode);
        log.info("==========导入已完成！==========");
    }
}
