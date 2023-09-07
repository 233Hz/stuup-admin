package com.poho.stuup.handle.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.service.RecHonorService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 个人荣誉记录填报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecHonorListener implements ReadListener<RecHonorExcel> {

    private final StudentMapper studentMapper;
    private final RecHonorService recHonorService;
    private final GrowthItem growthItem;
    private final Long yearId;
    private final Long semesterId;
    private final Long userId;
    private final long batchCode;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecHonorExcel> recHonorExcels = new ArrayList<>();

    public RecHonorListener(StudentMapper studentMapper, RecHonorService recHonorService, GrowthItem growthItem, Long yearId, Long semesterId, Long userId, long batchCode) {
        this.studentMapper = studentMapper;
        this.recHonorService = recHonorService;
        this.growthItem = growthItem;
        this.yearId = yearId;
        this.semesterId = semesterId;
        this.userId = userId;
        this.batchCode = batchCode;
    }

    @Override
    public void invoke(RecHonorExcel data, AnalysisContext context) {
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
        if (StrUtil.isBlank(data.getName())) {
            errorMessages.add("荣誉称号不能为空");
        }
        if (StrUtil.isBlank(data.getLevel())) {
            errorMessages.add("级别不能为空");
        }
        if (RecLevelEnum.getValueForLabel(data.getLevel()) == null) {
            errorMessages.add("级别不存在");
        }
        if (StrUtil.isBlank(data.getUnit())) {
            errorMessages.add("评选单位不能为空");
        }
        if (StrUtil.isBlank(data.getTime())) {
            errorMessages.add("评选时间不能为空");
        }
        if (!Utils.isDate(data.getTime())) {
            errorMessages.add("评选时间格式不正确");
        }

        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            success++;
            recHonorExcels.add(data);
        } else {
            fail++;
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        recHonorService.saveRecHonorExcel(recHonorExcels, growthItem, yearId, semesterId, userId, batchCode);
        log.info("==========导入已完成！==========");
    }
}
