package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.handle.excel.RecSocietyListener;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.ExcelError;
import com.poho.stuup.model.excel.RecSocietyExcel;
import com.poho.stuup.service.RecSocietyService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 参加社团记录填报导入处理
 * @date 2023/5/25 14:56
 */
@Slf4j
public class RecSocietyHandle implements RecExcelHandle {

    @Override
    public ResponseModel<List<ExcelError>> recImport(MultipartFile file, GrowthItem growthItem, Map<String, Object> params) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecSocietyService recSocietyService = SpringContextHolder.getBean(RecSocietyService.class);
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecSocietyListener recSocietyListener = new RecSocietyListener(start, params, growthItem, studentMapper, recSocietyService);
            EasyExcel.read(file.getInputStream(), RecSocietyExcel.class, recSocietyListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:{}ms", end - start);
            log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
            if (recSocietyListener.total == 0) {
                return ResponseModel.failed("Excel为空！");
            }
            if (CollUtil.isNotEmpty(recSocietyListener.errors)) {
                return ResponseModel.ok(recSocietyListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recSocietyListener.total, recSocietyListener.success, recSocietyListener.fail));
            }
            return ResponseModel.ok(null, "导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }


}
