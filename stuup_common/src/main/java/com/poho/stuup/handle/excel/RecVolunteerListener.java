package com.poho.stuup.handle.excel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.service.RecVolunteerService;
import com.poho.stuup.util.Utils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 志愿者活动记录填报导入
 * @date 2023/5/25 14:58
 */
@Slf4j
public class RecVolunteerListener implements ReadListener<RecVolunteerExcel> {

    private final long batchCode;
    private final Map<String, Object> params;
    private final GrowthItem growthItem;
    private final StudentMapper studentMapper;
    private final RecVolunteerService recVolunteerService;

    //===============================================================

    public int total, success, fail;
    public List<ExcelError> errors = new ArrayList<>();
    private final Map<String, Long> studentMap = new HashMap<>();
    private final List<RecVolunteerExcel> recVolunteerExcels = new ArrayList<>();

    public RecVolunteerListener(long batchCode, Map<String, Object> params, GrowthItem growthItem, StudentMapper studentMapper, RecVolunteerService recVolunteerService) {
        this.batchCode = batchCode;
        this.params = params;
        this.growthItem = growthItem;
        this.studentMapper = studentMapper;
        this.recVolunteerService = recVolunteerService;
    }

    @Override
    public void invoke(RecVolunteerExcel data, AnalysisContext context) {
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
            errorMessages.add("基地/项目名称不能为空");
        }
        if (StrUtil.isBlank(data.getLevel())) {
            errorMessages.add("级别不能为空");
        }
        if (StrUtil.isBlank(data.getChild())) {
            errorMessages.add("子项目不能为空");
        }
        if (StrUtil.isBlank(data.getPost())) {
            errorMessages.add("岗位不能为空");
        }
        if (StrUtil.isBlank(data.getStudyTime())) {
            errorMessages.add("学时不能为空");
        }
        if (!Utils.isNumber(data.getStudyTime())) {
            errorMessages.add("学时必须为数字");
        }
        if (StrUtil.isBlank(data.getServiceTime())) {
            errorMessages.add("服务时间不能为空");
        }
        if (!Utils.isDate(data.getServiceTime())) {
            errorMessages.add("服务时间必须为日期");
        }


        if (CollUtil.isEmpty(errorMessages)) {
            log.info("==========解析到一条数据:{}", JSON.toJSONString(data));
            success++;
            recVolunteerExcels.add(data);
        } else {
            fail++;
            this.errors.add(ExcelError.builder().lineNum(rowIndex).errors(JSON.toJSONString(errorMessages)).build());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        recVolunteerService.saveRecVolunteerExcel(batchCode, growthItem, recVolunteerExcels, params);
        log.info("==========导入已完成！==========");
    }
}
