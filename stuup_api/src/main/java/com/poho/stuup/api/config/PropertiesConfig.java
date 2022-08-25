package com.poho.stuup.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author wupeng
 */
@Component
public class PropertiesConfig {
    @Value("${stuup.base.doc}")
    private String baseDoc;
    @Value("${stuup.base.url}")
    private String baseUrl;

    public String getBaseDoc() {
        return baseDoc;
    }

    public void setBaseDoc(String baseDoc) {
        this.baseDoc = baseDoc;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
