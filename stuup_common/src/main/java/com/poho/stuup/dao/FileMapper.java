package com.poho.stuup.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.poho.stuup.model.File;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 文件管理表 Mapper 接口
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-16
 */
@Mapper
public interface FileMapper extends BaseMapper<File> {

}
