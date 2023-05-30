package com.poho.stuup.model.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

/**
 * @author BUNGA
 * @description: 前端选择器对象
 * @date 2023/5/30 18:35
 */
@Getter
@Setter
@Builder
public class SelectorVO {

    private String label;

    private Objects value;
}
