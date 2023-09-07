package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.RecVolunteerMapper;
import com.poho.stuup.dao.SemesterMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.handle.excel.RecVolunteerListener;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.service.RecVolunteerService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 志愿者活动记录填报导入处理
 * @date 2023/5/25 14:56
 */
@Slf4j
public class RecVolunteerHandle implements RecExcelHandle {

    @Override
    public ResponseModel<List<ExcelError>> recImport(MultipartFile file, GrowthItem growthItem, Long userId) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecVolunteerService recVolunteerService = SpringContextHolder.getBean(RecVolunteerService.class);
            YearMapper yearMapper = SpringContextHolder.getBean(YearMapper.class);
            SemesterMapper semesterMapper = SpringContextHolder.getBean(SemesterMapper.class);
            Year year = yearMapper.getCurrentYear();
            if (year == null) return ResponseModel.failed("不在当前学年时间段内");
            Semester semester = semesterMapper.selectOne(Wrappers.<Semester>lambdaQuery().eq(Semester::getIsCurrent, WhetherEnum.YES.getValue()));
            if (semester == null) return ResponseModel.failed("不在当前学期时间段内");
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecVolunteerListener recVolunteerListener = new RecVolunteerListener(studentMapper, recVolunteerService, growthItem, year.getOid(), semester.getId(), userId, start);
            EasyExcel.read(file.getInputStream(), RecVolunteerExcel.class, recVolunteerListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:{}ms", end - start);
            log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
            if (recVolunteerListener.total == 0) {
                return ResponseModel.failed("Excel为空！");
            }
            if (CollUtil.isNotEmpty(recVolunteerListener.errors)) {
                return ResponseModel.ok(recVolunteerListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recVolunteerListener.total, recVolunteerListener.success, recVolunteerListener.fail));
            }
            return ResponseModel.ok(null, "导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }

    @Override
    public void recExport(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecVolunteerMapper recVolunteerMapper = SpringContextHolder.getBean(RecVolunteerMapper.class);
        List<RecVolunteerExcel> recVolunteerExcels = recVolunteerMapper.queryExcelList(params);
        recVolunteerExcels.forEach(recVolunteerExcel -> {
            Integer level = recVolunteerExcel.getLevelValue();
            recVolunteerExcel.setLevel(RecLevelEnum.getLabelForValue(level));
        });
        EasyExcel.write(response.getOutputStream(), RecVolunteerExcel.class).sheet().doWrite(recVolunteerExcels);
    }

}
