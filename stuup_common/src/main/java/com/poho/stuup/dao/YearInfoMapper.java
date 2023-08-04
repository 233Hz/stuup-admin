package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.YearInfo;
import com.poho.stuup.model.vo.YearAtSchoolNumVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 年份信息表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-07-19
 */
@Mapper
public interface YearInfoMapper extends BaseMapper<YearInfo> {

    /**
     * 统计近三年在校生人数
     *
     * @return
     */
    List<YearAtSchoolNumVO> countNear3YearsAtSchoolNum();
}
