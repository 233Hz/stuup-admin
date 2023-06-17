package com.poho.stuup.model.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author BUNGA
 * @description: 设置项目负责人对象
 * @date 2023/6/14 18:54
 */
@Data
public class GrowthItemUserDTO {

    /**
     * 项目id
     */
    @NotNull(message = "请选择要设置负责人的成长项目")
    private Long growId;

    /**
     * 负责人id集合
     */
    @NotEmpty(message = "请选择要设置的项目负责人")
    private List<Long> userIds;
}
