package com.poho.stuup.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.poho.stuup.dao.FileMapper;
import com.poho.stuup.model.File;
import com.poho.stuup.service.FileService;
import com.poho.stuup.util.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件管理表 服务实现类
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-16
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Override
    public String getFileUrl(Long id) throws Exception {
        File file = baseMapper.selectById(id);
        String bucket = file.getBucket();
        String storageName = file.getStorageName();
        return MinioUtils.getPreSignedObjectUrl(bucket, storageName);
    }
}
