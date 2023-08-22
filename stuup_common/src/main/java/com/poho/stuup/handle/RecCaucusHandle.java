package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.RecCaucusMapper;
import com.poho.stuup.dao.SemesterMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.handle.excel.RecCaucusListener;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.service.RecCaucusService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 参加党团学习项目记录填报导入处理
 * @date 2023/5/25 13:41
 */
@Slf4j
public class RecCaucusHandle implements RecExcelHandle {

    @Override
    public ResponseModel<List<ExcelError>> recImport(MultipartFile file, GrowthItem growthItem, Long userId) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecCaucusService recCaucusService = SpringContextHolder.getBean(RecCaucusService.class);
            YearMapper yearMapper = SpringContextHolder.getBean(YearMapper.class);
            SemesterMapper semesterMapper = SpringContextHolder.getBean(SemesterMapper.class);
            Year year = yearMapper.findCurrYear();
            if (year == null) return ResponseModel.failed("不在当前学年时间段内");
            Semester semester = semesterMapper.selectOne(Wrappers.<Semester>lambdaQuery().eq(Semester::getIsCurrent, WhetherEnum.YES.getValue()));
            if (semester == null) return ResponseModel.failed("不在当前学期时间段内");
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecCaucusListener recCaucusListener = new RecCaucusListener(studentMapper, recCaucusService, growthItem, year.getOid(), semester.getId(), userId, start);
            EasyExcel.read(file.getInputStream(), RecCaucusExcel.class, recCaucusListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:{}ms", end - start);
            log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
            if (recCaucusListener.total == 0) {
                return ResponseModel.failed("Excel为空！");
            }
            if (CollUtil.isNotEmpty(recCaucusListener.errors)) {
                return ResponseModel.ok(recCaucusListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recCaucusListener.total, recCaucusListener.success, recCaucusListener.fail));
            }
            return ResponseModel.ok(null, "导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }

    @Override
    public void recExport(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecCaucusMapper recCaucusMapper = SpringContextHolder.getBean(RecCaucusMapper.class);
        List<RecCaucusExcel> recCaucusExcels = recCaucusMapper.queryExcelList(params);
        recCaucusExcels.forEach(recCaucusExcel -> {
            Integer level = recCaucusExcel.getLevelValue();
            Integer role = recCaucusExcel.getRoleValue();
            recCaucusExcel.setLevel(RecLevelEnum.getLabelForValue(level));
            recCaucusExcel.setRole(RecRoleEnum.getRoleForValue(role));
        });
        EasyExcel.write(response.getOutputStream(), RecCaucusExcel.class).sheet().doWrite(recCaucusExcels);
    }


}
