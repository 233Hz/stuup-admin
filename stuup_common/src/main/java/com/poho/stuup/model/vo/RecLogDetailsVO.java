package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author BUNGA
 * @description: 导入日志详情页面对象
 * @date 2023/6/2 16:28
 */
@Data
public class RecLogDetailsVO<T, K> {

    private List<T> tableConfig;

    private List<K> tableData;


}
