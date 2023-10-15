package com.poho.stuup.growth;

import com.alibaba.excel.EasyExcel;
import com.poho.stuup.constant.RecCodeConstants;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.RecSocietyMapper;
import com.poho.stuup.listener.excel.RecSocietyListener;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.excel.RecSocietyExcel;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RecCode(code = RecCodeConstants.REC_SOCIETY)
public class RecSocietyStrategy implements GrowthStrategy<RecSocietyExcel> {
    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecSocietyListener recSocietyListener = new RecSocietyListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecSocietyExcel.class, recSocietyListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recSocietyListener.total)
                .success(recSocietyListener.success)
                .fail(recSocietyListener.fail)
                .errors(recSocietyListener.errors)
                .build();
    }

    @Override
    public void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecSocietyMapper recSocietyMapper = SpringContextHolder.getBean(RecSocietyMapper.class);
        List<RecSocietyExcel> exportData = recSocietyMapper.selectExportData(params);
        exportData.forEach(recSocietyExcel -> {
            Integer level = recSocietyExcel.getLevelValue();
            Integer role = recSocietyExcel.getRoleValue();
            recSocietyExcel.setLevel(RecLevelEnum.getLabelForValue(level));
            recSocietyExcel.setRole(RecRoleEnum.getRoleForValue(role));
        });
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(exportData);
    }

}
