package com.poho.stuup.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

/**
 * 树形对象
 *
 * @author BUNGA
 * @description: TODO
 * @date 2023/5/23 9:15
 */
@Data
public class Tree {

    private String key;

    private Object value;

    private List<Tree> children;

    @JsonIgnore
    private Integer sort;

}
