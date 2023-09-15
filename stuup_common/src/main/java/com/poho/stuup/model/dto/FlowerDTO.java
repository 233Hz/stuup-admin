package com.poho.stuup.model.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author BUNGA
 * @date 2023/6/7 11:12
 */
@Data
public class FlowerDTO {

    @NotBlank(message = "要设置的花朵不能为空")
    private String key;

    @NotBlank(message = "要设置的值不能为空")
    private String value;

}
