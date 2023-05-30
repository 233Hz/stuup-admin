package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.handle.excel.RecLaborTimeListener;
import com.poho.stuup.model.excel.RecLaborTimeExcel;
import com.poho.stuup.service.RecLaborTimeService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 生产劳动实践记录填报导入处理
 * @date 2023/5/25 14:56
 */
@Slf4j
public class RecLaborTimeHandle implements RecExcelHandle {

    @Override
    public ResponseModel recImport(MultipartFile file, Map<String, Object> params) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecLaborTimeService recLaborTimeService = SpringContextHolder.getBean(RecLaborTimeService.class);
            long start = System.currentTimeMillis();
            RecLaborTimeListener recLaborTimeListener = new RecLaborTimeListener(studentMapper, recLaborTimeService, start);
            EasyExcel.read(file.getInputStream(), RecLaborTimeExcel.class, recLaborTimeListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:" + (end - start) / 1000 + "s");
            if (CollUtil.isNotEmpty(recLaborTimeListener.errors)) {
                return ResponseModel.failed(recLaborTimeListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recLaborTimeListener.total, recLaborTimeListener.success, recLaborTimeListener.fail));
            }
            return ResponseModel.ok("导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }
}
