package com.poho.stuup.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ConvertUtil {

    private ConvertUtil() {
    }

    /**
     * 将List映射为List，比如List<Person> personList转为List<String> nameList
     *
     * @param originList 原数据
     * @param mapper     映射规则
     * @param <T>        原数据的元素类型
     * @param <R>        新数据的元素类型
     * @return
     */
    public static <T, R> List<R> resultToList(List<T> originList,
                                              Function<T, R> mapper) {
        if (originList == null || originList.isEmpty()) {
            return new ArrayList<>();
        }

        List<R> newList = new ArrayList<>(originList.size());
        for (T originElement : originList) {
            R newElement = mapper.apply(originElement);
            if (newElement == null) {
                continue;
            }
            newList.add(newElement);
        }

        return newList;
    }

    /**
     * 将List转为Map
     *
     * @param list         原数据
     * @param keyExtractor Key的抽取规则
     * @param <K>          Key
     * @param <V>          Value
     * @return
     */
    public static <K, V> Map<K, V> listToMap(List<V> list,
                                             Function<V, K> keyExtractor) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }

        Map<K, V> map = new HashMap<>(list.size());
        for (V element : list) {
            K key = keyExtractor.apply(element);
            if (key == null) {
                continue;
            }
            map.put(key, element);
        }

        return map;
    }
}