package com.poho.stuup.growth;

import com.alibaba.excel.EasyExcel;
import com.poho.stuup.constant.RecCodeConstants;
import com.poho.stuup.constant.RecLevelEnum;
import com.poho.stuup.dao.RecNationMapper;
import com.poho.stuup.listener.excel.RecNationListener;
import com.poho.stuup.model.excel.RecHonorExcel;
import com.poho.stuup.model.excel.RecNationExcel;
import com.poho.stuup.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RecCode(code = RecCodeConstants.REC_NATION)
public class RecNationStrategy implements GrowthStrategy<RecNationExcel> {
    @Override
    public RecImportResult importHandler(MultipartFile file, RecImportParams params) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start("解析excel");
        RecNationListener recNationListener = new RecNationListener(params, stopWatch);
        try {
            EasyExcel.read(file.getInputStream(), RecNationExcel.class, recNationListener).sheet().doRead();
            stopWatch.stop();

            log.info(stopWatch.getTotalTimeMillis() + " ms");
            log.info(stopWatch.getTotalTimeSeconds() + " s");
            log.info(stopWatch.prettyPrint());
        } catch (IOException e) {
            throw new RuntimeException("读取excel失败");
        }
        return RecImportResult
                .builder()
                .total(recNationListener.total)
                .success(recNationListener.success)
                .fail(recNationListener.fail)
                .errors(recNationListener.errors)
                .build();
    }

    @Override
    public void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        RecNationMapper nationMapper = SpringContextHolder.getBean(RecNationMapper.class);
        List<RecNationExcel> exportData = nationMapper.selectExportData(params);
        exportData.forEach(recNationExcel -> {
            Integer level = recNationExcel.getLevelValue();
            recNationExcel.setLevel(RecLevelEnum.getLabelForValue(level));
        });
        EasyExcel.write(response.getOutputStream(), RecHonorExcel.class).sheet().doWrite(exportData);
    }

}
