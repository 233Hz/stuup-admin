package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.RankSemester;

import java.util.Date;

/**
 * <p>
 * 学期榜 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-28
 */
public interface RankSemesterService extends IService<RankSemester> {

    void generateRank(Date date);
}
