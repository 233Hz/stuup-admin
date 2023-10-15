package com.poho.stuup.listener.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.constant.MilitaryLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.service.RecMilitaryService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

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

    private final StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
    private final RecMilitaryService recMilitaryService = SpringContextHolder.getBean(RecMilitaryService.class);
    private final RecImportParams params;
    private final StopWatch stopWatch;


    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecMilitaryExcel> recMilitaryExcels = new ArrayList<>();

    public RecMilitaryListener(RecImportParams params, StopWatch stopWatch) {
        this.params = params;
        this.stopWatch = stopWatch;
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
        stopWatch.stop();
        stopWatch.start("保存数据");
        recMilitaryService.saveRecMilitaryExcel(recMilitaryExcels, params);
        log.info("==========导入已完成！==========");
    }
}
