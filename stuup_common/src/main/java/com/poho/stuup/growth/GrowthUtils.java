package com.poho.stuup.growth;

import com.poho.stuup.model.GrowthItem;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@UtilityClass
public class GrowthUtils {

    public RecImportResult recImport(String handle, MultipartFile file, RecImportParams params) {
        GrowthItem growthItem = params.getGrowthItem();
        if (params.getUserId() == null) {
            throw new RuntimeException("缺少参数userId");
        } else if (params.getYearId() == null) {
            throw new RuntimeException("缺少参数yearId");
        } else if (params.getSemesterId() == null) {
            throw new RuntimeException("缺少参数semesterId");
        } else if (growthItem == null) {
            throw new RuntimeException("缺少参数growthItem");
        }

        if (growthItem.getId() == null) {
            throw new RuntimeException("缺少参数growthItem.id");
        } else if (growthItem.getCode() == null) {
            throw new RuntimeException("缺少参数growthItem.code");
        } else if (growthItem.getScorePeriod() == null) {
            throw new RuntimeException("缺少参数growthItem.getScorePeriod");
        } else if (growthItem.getCalculateType() == null) {
            throw new RuntimeException("缺少参数growthItem.calculateType");
        } else if (growthItem.getScore() == null) {
            throw new RuntimeException("缺少参数growthItem.score");
        }

        GrowthStrategy growthStrategy = GrowthFactory.getInstance().createGrowthStrategy(handle);
        return growthStrategy.importHandler(file, params);
    }

    @SneakyThrows
    public void recExport(HttpServletResponse response, String recCode, Map<String, Object> params) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        GrowthStrategy<?> growthStrategy = GrowthFactory.getInstance().createGrowthStrategy(recCode);
        growthStrategy.exportHandler(response, params);
    }

}
