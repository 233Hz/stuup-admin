package com.poho.stuup.custom;

import lombok.Data;

@Data
public class GrowthReportConfig {

    private EthicsAndCitizenshipConfig ethicsAndCitizenship;

    @Data
    public static class EthicsAndCitizenshipConfig {

    }

}