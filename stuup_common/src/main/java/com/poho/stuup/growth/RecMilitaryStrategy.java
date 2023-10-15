package com.poho.stuup.growth;

import com.alibaba.excel.EasyExcel;
import com.poho.stuup.constant.RecCodeConstants;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.WhetherEnum;
import com.poho.stuup.dao.RecMilitaryMapper;
import com.poho.stuup.listener.excel.RecMilitaryListener;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.excel.RecMilitaryExcel;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RecCode(code = RecCodeConstants.REC_MILITARY)
public class RecMilitaryStrategy implements GrowthStrategy<RecMilitaryExcel> {
    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecMilitaryListener recMilitaryListener = new RecMilitaryListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecMilitaryExcel.class, recMilitaryListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recMilitaryListener.total)
                .success(recMilitaryListener.success)
                .fail(recMilitaryListener.fail)
                .errors(recMilitaryListener.errors)
                .build();
    }

    @Override
    public void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecMilitaryMapper recMilitaryMapper = SpringContextHolder.getBean(RecMilitaryMapper.class);
        List<RecMilitaryExcel> exportData = recMilitaryMapper.selectExportData(params);
        exportData.forEach(recMilitaryExcel -> {
            Integer level = recMilitaryExcel.getLevelValue();
            Integer excellent = recMilitaryExcel.getExcellentValue();
            recMilitaryExcel.setLevel(RecLevelEnum.getLabelForValue(level));
            recMilitaryExcel.setExcellent(WhetherEnum.getLabelForValue(excellent));
        });
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(exportData);
    }

}
