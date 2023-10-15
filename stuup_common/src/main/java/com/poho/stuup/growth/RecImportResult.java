package com.poho.stuup.growth;

import com.poho.stuup.model.excel.ExcelError;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class RecImportResult {

    private int total;

    private int success;

    private int fail;

    private List<ExcelError> errors;

}
