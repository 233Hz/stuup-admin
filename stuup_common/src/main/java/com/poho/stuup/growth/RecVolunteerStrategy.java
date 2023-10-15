package com.poho.stuup.growth;

import com.alibaba.excel.EasyExcel;
import com.poho.stuup.constant.RecCodeConstants;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecVolunteerMapper;
import com.poho.stuup.listener.excel.RecVolunteerListener;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.excel.RecVolunteerExcel;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RecCode(code = RecCodeConstants.REC_VOLUNTEER)
public class RecVolunteerStrategy implements GrowthStrategy<RecVolunteerExcel> {
    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecVolunteerListener recVolunteerListener = new RecVolunteerListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecVolunteerExcel.class, recVolunteerListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recVolunteerListener.total)
                .success(recVolunteerListener.success)
                .fail(recVolunteerListener.fail)
                .errors(recVolunteerListener.errors)
                .build();
    }

    @Override
    public void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecVolunteerMapper recVolunteerMapper = SpringContextHolder.getBean(RecVolunteerMapper.class);
        List<RecVolunteerExcel> exportData = recVolunteerMapper.selectExportData(params);
        exportData.forEach(recVolunteerExcel -> {
            Integer level = recVolunteerExcel.getLevelValue();
            recVolunteerExcel.setLevel(RecLevelEnum.getLabelForValue(level));
        });
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(exportData);
    }

}
