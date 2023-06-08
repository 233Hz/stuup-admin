package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.handle.excel.RecMilitaryListener;
import com.poho.stuup.model.GrowthItem;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.service.RecMilitaryService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 军事训练记录填报报导入处理
 * @date 2023/5/25 14:56
 */
@Slf4j
public class RecMilitaryHandle implements RecExcelHandle {

    @Override
    public ResponseModel recImport(MultipartFile file, GrowthItem growthItem, Map<String, Object> params) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecMilitaryService recMilitaryService = SpringContextHolder.getBean(RecMilitaryService.class);
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecMilitaryListener recMilitaryListener = new RecMilitaryListener(start, params, growthItem, studentMapper, recMilitaryService);
            EasyExcel.read(file.getInputStream(), RecMilitaryExcel.class, recMilitaryListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:{}ms", end - start);
            log.info("耗时:{}分{}秒", (end - start) / 1000 / 60, (end - start) / 1000 % 60);
            if (recMilitaryListener.total == 0) {
                return ResponseModel.failed("Excel为空！");
            }
            if (CollUtil.isNotEmpty(recMilitaryListener.errors)) {
                return ResponseModel.ok(recMilitaryListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recMilitaryListener.total, recMilitaryListener.success, recMilitaryListener.fail));
            }
            return ResponseModel.ok(null, "导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }

}
