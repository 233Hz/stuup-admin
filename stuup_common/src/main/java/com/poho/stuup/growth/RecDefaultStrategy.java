package com.poho.stuup.growth;

import com.alibaba.excel.EasyExcel;
import com.poho.stuup.listener.excel.RecDefaultListener;
import com.poho.stuup.model.excel.RecDefaultExcel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RecCode()
public class RecDefaultStrategy implements GrowthStrategy<RecDefaultExcel> {
    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecDefaultListener recDefaultListener = new RecDefaultListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecDefaultExcel.class, recDefaultListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recDefaultListener.total)
                .success(recDefaultListener.success)
                .fail(recDefaultListener.fail)
                .errors(recDefaultListener.errors)
                .build();
    }
}
