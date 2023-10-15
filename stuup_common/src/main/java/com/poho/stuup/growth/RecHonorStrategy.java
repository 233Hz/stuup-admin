package com.poho.stuup.growth;

import com.alibaba.excel.EasyExcel;
import com.poho.stuup.constant.RecCodeConstants;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecHonorMapper;
import com.poho.stuup.listener.excel.RecHonorListener;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RecCode(code = RecCodeConstants.REC_HONOR)
public class RecHonorStrategy implements GrowthStrategy<RecHonorExcel> {

    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecHonorListener recHonorListener = new RecHonorListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecHonorExcel.class, recHonorListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recHonorListener.total)
                .success(recHonorListener.success)
                .fail(recHonorListener.fail)
                .errors(recHonorListener.errors)
                .build();
    }

    @Override
    public void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecHonorMapper recHonorMapper = SpringContextHolder.getBean(RecHonorMapper.class);
        List<RecHonorExcel> exportData = recHonorMapper.selectExportData(params);
        exportData.forEach(recHonorExcel -> {
            Integer level = recHonorExcel.getLevelValue();
            recHonorExcel.setLevel(RecLevelEnum.getLabelForValue(level));
        });
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(exportData);
    }
}
