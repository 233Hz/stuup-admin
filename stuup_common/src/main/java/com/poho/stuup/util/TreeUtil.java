package com.poho.stuup.util;

import lombok.experimental.UtilityClass;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author BUNGA
 * @description
 * @date 2023/9/19 14:03
 */
@UtilityClass
public class TreeUtil {

    public <T> void sortTree(List<T> list, Function<T, List<T>> childrenGetter, Comparator<T> comparator) {
        list.sort(comparator);
        for (T item : list) {
            List<T> children = childrenGetter.apply(item);
            if (children != null && !children.isEmpty()) {
                sortTree(children, childrenGetter, comparator);
            }
        }
    }
}
