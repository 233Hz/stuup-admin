package com.poho.stuup.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.poho.stuup.model.File;

/**
 * <p>
 * 文件管理表 服务类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-16
 */
public interface FileService extends IService<File> {

    String getFileUrl(Long id) throws Exception;
}
