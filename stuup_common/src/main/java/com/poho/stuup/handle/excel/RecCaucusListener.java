package com.poho.stuup.handle.excel;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.service.RecCaucusService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BUNGA
 * @description: 参加党团学习项目记录填报导入
 * @date 2023/5/25 13:47
 */
@Slf4j
public class RecCaucusListener implements ReadListener<RecCaucusExcel> {

    private final StudentMapper studentMapper;
    private final RecCaucusService recCaucusService;
    private final long batchCode;

    /**
     * 导入错误消息
     */
    public List<ExcelError> errors = new ArrayList<>();

    public int total, success, fail;

    public RecCaucusListener(StudentMapper studentMapper, RecCaucusService recCaucusService, long batchCode) {
        this.studentMapper = studentMapper;
        this.recCaucusService = recCaucusService;
        this.batchCode = batchCode;
    }


    @Override
    public void invoke(RecCaucusExcel data, AnalysisContext context) {
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
        if (StrUtil.isBlank(data.getName())) {
            errorMessages.add("项目名称不能为空");
        }
        if (StrUtil.isBlank(data.getLevel())) {
            errorMessages.add("项目级别不能为空");
        }
        if (RecLevelEnum.getLabelValue(data.getLevel()) == null) {
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
        if (RecRoleEnum.getRoleValue(data.getRole()) == null) {
            errorMessages.add("角色不存在");
        }

        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            data.setBatchCode(batchCode);
            boolean flag = recCaucusService.saveData(data);
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
