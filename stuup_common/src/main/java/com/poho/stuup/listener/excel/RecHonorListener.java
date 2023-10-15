package com.poho.stuup.listener.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.service.RecHonorService;
import com.poho.stuup.util.SpringContextHolder;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

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

    private final StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
    private final RecHonorService recHonorService = SpringContextHolder.getBean(RecHonorService.class);
    private final RecImportParams params;
    private final StopWatch stopWatch;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecHonorExcel> recHonorExcels = new ArrayList<>();

    public RecHonorListener(RecImportParams params, StopWatch stopWatch) {
        this.params = params;
        this.stopWatch = stopWatch;
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
        stopWatch.stop();
        stopWatch.start("保存数据");
        recHonorService.saveRecHonorExcel(recHonorExcels, params);
        log.info("==========导入已完成！==========");
    }
}
