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
import com.poho.stuup.model.excel.RecNationExcel;
import com.poho.stuup.service.RecNationService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 参加国防民防项目记录填报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecNationListener implements ReadListener<RecNationExcel> {

    private final long batchCode;
    private final Map<String, Object> params;
    private final GrowthItem growthItem;
    private final StudentMapper studentMapper;
    private final RecNationService recNationService;

    //================================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecNationExcel> recNationExcels = new ArrayList<>();

    public RecNationListener(long batchCode, Map<String, Object> params, GrowthItem growthItem, StudentMapper studentMapper, RecNationService recNationService) {
        this.batchCode = batchCode;
        this.params = params;
        this.growthItem = growthItem;
        this.studentMapper = studentMapper;
        this.recNationService = recNationService;
    }

    @Override
    public void invoke(RecNationExcel data, AnalysisContext context) {
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
            errorMessages.add("获奖级别不能为空");
        }
        if (RecLevelEnum.getLabelValue(data.getLevel()) == null) {
            errorMessages.add("获奖级别不存在");
        }
        if (StrUtil.isBlank(data.getOrg())) {
            errorMessages.add("组织机构（主办方）不能为空");
        }
        if (StrUtil.isBlank(data.getHour())) {
            errorMessages.add("累计时间（课时）不能为空");
        }
        if (!Utils.isNumber(data.getHour())) {
            errorMessages.add("累计时间（课时）必须为数字");
        }


        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            success++;
            recNationExcels.add(data);
        } else {
            fail++;
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        recNationService.saveRecNationExcel(batchCode, growthItem, recNationExcels, params);
        log.info("==========导入已完成！==========");
    }
}
