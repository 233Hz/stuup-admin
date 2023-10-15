package com.poho.stuup.growth;

import com.alibaba.excel.EasyExcel;
import com.poho.stuup.constant.RecCodeConstants;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.constant.RecRoleEnum;
import com.poho.stuup.dao.RecCaucusMapper;
import com.poho.stuup.listener.excel.RecCaucusListener;
import com.poho.stuup.model.excel.RecCaucusExcel;
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
@RecCode(code = RecCodeConstants.REC_CAUCUS)
public class RecRecCaucusStrategy implements GrowthStrategy<RecCaucusExcel> {

    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecCaucusListener recCaucusListener = new RecCaucusListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecCaucusExcel.class, recCaucusListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recCaucusListener.total)
                .success(recCaucusListener.success)
                .fail(recCaucusListener.fail)
                .errors(recCaucusListener.errors)
                .build();
    }

    @Override
    public void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecCaucusMapper recCaucusMapper = SpringContextHolder.getBean(RecCaucusMapper.class);
        List<RecCaucusExcel> exportData = recCaucusMapper.selectExportData(params);
        exportData.forEach(recCaucusExcel -> {
            Integer level = recCaucusExcel.getLevelValue();
            Integer role = recCaucusExcel.getRoleValue();
            recCaucusExcel.setLevel(RecLevelEnum.getLabelForValue(level));
            recCaucusExcel.setRole(RecRoleEnum.getRoleForValue(role));
        });
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(exportData);
    }

}
