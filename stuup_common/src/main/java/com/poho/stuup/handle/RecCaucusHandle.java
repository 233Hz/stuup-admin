package com.poho.stuup.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.poho.common.custom.ResponseModel;
import com.poho.stuup.dao.StudentMapper;
import com.poho.stuup.handle.excel.RecCaucusListener;
import com.poho.stuup.model.excel.RecCaucusExcel;
import com.poho.stuup.service.RecCaucusService;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

/**
 * @author BUNGA
 * @description: 参加党团学习项目记录填报导入处理
 * @date 2023/5/25 13:41
 */
@Slf4j
public class RecCaucusHandle implements RecExcelHandle {

    @Override
    public ResponseModel recImport(MultipartFile file, Map<String, Object> params) {
        try {
            StudentMapper studentMapper = SpringContextHolder.getBean(StudentMapper.class);
            RecCaucusService recCaucusService = SpringContextHolder.getBean(RecCaucusService.class);
            log.info("开始导入");
            long start = System.currentTimeMillis();
            RecCaucusListener recCaucusListener = new RecCaucusListener(studentMapper, recCaucusService, start);
            EasyExcel.read(file.getInputStream(), RecCaucusExcel.class, recCaucusListener).sheet().doRead();
            long end = System.currentTimeMillis();
            log.info("耗时:" + (end - start) / 1000 + "s");
            if (CollUtil.isNotEmpty(recCaucusListener.errors)) {
                return ResponseModel.ok(recCaucusListener.errors, StrUtil.format("导入成功[总条数：{}，成功：{}，失败：{}]", recCaucusListener.total, recCaucusListener.success, recCaucusListener.fail));
            }
            return ResponseModel.ok("导入成功");
        } catch (IOException e) {
            return ResponseModel.failed("导入失败");
        }
    }
}
