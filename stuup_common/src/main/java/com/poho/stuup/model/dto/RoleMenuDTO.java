package com.poho.stuup.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 设置角色菜单传输对象
 *
 * @author BUNGA
 * @date 2023/5/23 19:20
 */

@Getter
@Setter
public class RoleMenuDTO {

    @NotNull(message = "角色id不能为空")
    private Long roleId;

    @NotEmpty(message = "请选择要分配的菜单id")
    private List<Long> menuIds;
}
