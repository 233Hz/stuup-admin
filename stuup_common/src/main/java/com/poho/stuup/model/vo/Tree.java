package com.poho.stuup.model.vo;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 树形对象
 * @author BUNGA
 * @description: TODO
 * @date 2023/5/23 9:15
 */
@Data
public class Tree {

    private String key;

    private Object value;

    private List<Tree> children;
}
