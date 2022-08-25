package com.poho.common.util;

import java.util.Comparator;

/**
 *
 * @author wupeng
 * @date 2016/12/17
 */
public class MapKeyComparator implements Comparator<String> {
    @Override
    public int compare(String str1, String str2) {
        return str1.compareTo(str2);
    }
}
