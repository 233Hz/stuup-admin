package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.FileMapper;
import com.poho.stuup.model.File;
import com.poho.stuup.service.FileService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件管理表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-16
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

}
