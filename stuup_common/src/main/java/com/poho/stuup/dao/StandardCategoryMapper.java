package com.poho.stuup.dao;

import com.poho.stuup.model.StandardCategory;

import java.util.Map;

public interface StandardCategoryMapper extends BaseDao<StandardCategory> {
    /**
     *
     * @param param
     * @return
     */
    StandardCategory checkCategory(Map<String, Object> param);
}