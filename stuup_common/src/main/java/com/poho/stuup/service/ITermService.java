package com.poho.stuup.service;


import com.poho.common.custom.ResponseModel;

/**
 * 具体实现学期信息维护的业务逻辑层接口
 */
public interface ITermService {

    ResponseModel findDataPageResult(String key, int page, int pageSize);

  /*  *//**
     * 新增或修改学期
     *
     * @param term
     * @return
     *//*
    AjaxResponse saveOrUpdateTerm(Term term);

    *//**
     * 根据学期id获取学期
     *
     * @param id
     * @return
     *//*
    Term selectByPrimaryKey(String id);

    *//**
     * 获取有效的学期
     *
     * @return
     *//*
    List<Term> findTerms();

    *//**
     *
     * @param num
     * @return
     *//*
    List<Term> findMaxPublishTerms(int num);

    */
    int deleteByPrimaryKey(Integer id);

    /**
     * 查询当前学期
     *
     * @return
     *//*
    Term findCurrentTerm();*/
}
