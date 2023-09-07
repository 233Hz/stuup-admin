package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.RecHonorMapper;
import com.poho.stuup.dao.SemesterMapper;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.dao.YearMapper;
import com.poho.stuup.handle.excel.RecHonorListener;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.Semester;
import com.poho.stuup.model.Year;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.service.RecHonorService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 个人荣誉记录填报导入处理
 * @date 2023/5/25 14:56
 */
@Slf4j
public class RecHonorHandle implements RecExcelHandle {

    @Override
    public ResponseModel<List<ExcelError>> recImport(MultipartFile file, GrowthItem growthItem, Long userId) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecHonorService recHonorService = SpringContextHolder.getBean(RecHonorService.class);
            YearMapper yearMapper = SpringContextHolder.getBean(YearMapper.class);
            SemesterMapper semesterMapper = SpringContextHolder.getBean(SemesterMapper.class);
            Year year = yearMapper.getCurrentYear();
            if (year == null) return ResponseModel.failed("不在当前学年时间段内");
            Semester semester = semesterMapper.selectOne(Wrappers.<Semester>lambdaQuery().eq(Semester::getIsCurrent, WhetherEnum.YES.getValue()));
            if (semester == null) return ResponseModel.failed("不在当前学期时间段内");
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecHonorListener recHonorListener = new RecHonorListener(studentMapper, recHonorService, growthItem, year.getOid(), semester.getId(), userId, start);
            EasyExcel.read(file.getInputStream(), RecHonorExcel.class, recHonorListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:{}ms", end - start);
            log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
            if (recHonorListener.total == 0) {
                return ResponseModel.failed("Excel为空！");
            }
            if (CollUtil.isNotEmpty(recHonorListener.errors)) {
                return ResponseModel.ok(recHonorListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recHonorListener.total, recHonorListener.success, recHonorListener.fail));
            }
            return ResponseModel.ok(null, "导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }

    @Override
    public void recExport(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecHonorMapper recHonorMapper = SpringContextHolder.getBean(RecHonorMapper.class);
        List<RecHonorExcel> recHonorExcels = recHonorMapper.queryExcelList(params);
        recHonorExcels.forEach(recHonorExcel -> {
            Integer level = recHonorExcel.getLevelValue();
            recHonorExcel.setLevel(RecLevelEnum.getLabelForValue(level));
        });
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(recHonorExcels);
    }

}
