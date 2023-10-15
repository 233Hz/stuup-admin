package com.poho.stuup.growth;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface GrowthStrategy<T> {

    RecImportResult importHandler(MultipartFile file, RecImportParams params);

    default void exportHandler(HttpServletResponse response, Map<String, Object> params) throws IOException {
        throw new RuntimeException("该项目无法导出");
    }

}
