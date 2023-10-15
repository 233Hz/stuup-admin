package com.poho.stuup.listener.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.growth.RecImportParams;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.service.RecLaborTimeService;
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
 * @description: 生产劳动实践记录填报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecLaborTimeListener implements ReadListener<RecLaborTimeExcel> {

    private final StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
    private final RecLaborTimeService recLaborTimeService = SpringContextHolder.getBean(RecLaborTimeService.class);
    private final RecImportParams params;
    private final StopWatch stopWatch;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecLaborTimeExcel> recLaborTimeExcels = new ArrayList<>();

    public RecLaborTimeListener(RecImportParams params, StopWatch stopWatch) {
        this.params = params;
        this.stopWatch = stopWatch;
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
            studentId = studentMapper.getIdByStudentNo(studentNo);
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
        stopWatch.stop();
        stopWatch.start("保存数据");
        recLaborTimeService.saveRecLaborTimeExcel(recLaborTimeExcels, params);
        log.info("==========导入已完成！==========");
    }
}
