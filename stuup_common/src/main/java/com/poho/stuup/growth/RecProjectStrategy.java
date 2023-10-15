package com.poho.stuup.growth;


import com.alibaba.excel.EasyExcel;
import com.poho.stuup.constant.RecCodeConstants;
import com.poho.stuup.dao.RecProjectMapper;
import com.poho.stuup.listener.excel.RecProjectListener;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.excel.RecProjectExcel;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RecCode(code = RecCodeConstants.REC_PROJECT)
public class RecProjectStrategy implements GrowthStrategy<RecProjectExcel> {
    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecProjectListener recProjectListener = new RecProjectListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecProjectExcel.class, recProjectListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recProjectListener.total)
                .success(recProjectListener.success)
                .fail(recProjectListener.fail)
                .errors(recProjectListener.errors)
                .build();
    }

    @Override
    public void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecProjectMapper recProjectMapper = SpringContextHolder.getBean(RecProjectMapper.class);
        List<RecProjectExcel> exportData = recProjectMapper.selectExportData(params);
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(exportData);
    }

}
