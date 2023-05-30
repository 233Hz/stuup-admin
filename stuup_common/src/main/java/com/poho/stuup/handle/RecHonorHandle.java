package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.handle.excel.RecHonorListener;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.service.RecHonorService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 个人荣誉记录填报导入处理
 * @date 2023/5/25 14:56
 */
@Slf4j
public class RecHonorHandle implements RecExcelHandle {

    @Override
    public ResponseModel recImport(MultipartFile file, Map<String, Object> params) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecHonorService recHonorService = SpringContextHolder.getBean(RecHonorService.class);
            long start = System.currentTimeMillis();
            RecHonorListener recHonorListener = new RecHonorListener(studentMapper, recHonorService, start);
            EasyExcel.read(file.getInputStream(), RecHonorExcel.class, recHonorListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:" + (end - start) / 1000 + "s");
            if (CollUtil.isNotEmpty(recHonorListener.errors)) {
                return ResponseModel.failed(recHonorListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recHonorListener.total, recHonorListener.success, recHonorListener.fail));
            }
            return ResponseModel.ok("导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }
}