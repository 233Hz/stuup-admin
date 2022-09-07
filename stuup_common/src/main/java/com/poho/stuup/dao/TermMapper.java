package com.poho.stuup.dao;

import com.poho.stuup.model.Term;

import java.util.List;
import java.util.Map;

public interface TermMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Term record);

    int insertSelective(Term record);

    Term selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Term record);

    int updateByPrimaryKey(Term record);

    /**
     * 筛选后的学期个数
     *
     * @param map
     * @return
     */
    int findTotalTermByCond(Map<String, Object> map);

    /**
     * 筛选后的学期对象
     *
     * @param map
     * @return
     */
    List<Term> findTermPageResultByCond(Map<String, Object> map);

    /**
     * 获取有效的学期
     *
     * @return
     */
    List<Term> findTerms();

    /**
     *
     * @param num
     * @return
     */
    List<Term> findMaxPublishTerms(int num);

    /**
     *
     * @param term
     * @return
     */
    Term checkTerm(Term term);
}