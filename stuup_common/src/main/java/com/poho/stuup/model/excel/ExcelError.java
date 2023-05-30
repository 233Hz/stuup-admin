package com.poho.stuup.model.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author BUNGA
 * @description: Excel导入错误对象
 * @date 2023/5/25 13:53
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelError {
    private Integer lineNum;
    private String errors;
}