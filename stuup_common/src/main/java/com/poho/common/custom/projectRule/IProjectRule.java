package com.poho.common.custom.projectRule;


import com.poho.stuup.model.ScoreDetail;

public interface IProjectRule<T> {

    ScoreDetail handler(T obj);

}
