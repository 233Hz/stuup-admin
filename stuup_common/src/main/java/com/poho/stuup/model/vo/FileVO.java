package com.poho.stuup.model.vo;

import lombok.Data;

/**
 * @author BUNGA
 * @description: 上传文件返回对象
 * @date 2023/6/16 17:37
 */
@Data
public class FileVO {

    /**
     * 文件id
     */
    private Long id;

    /**
     * 文件原始名
     */
    private String storageName;

    /**
     * 文件存储名
     */
    private String originalName;

    /**
     * 文件存储桶名称
     */
    private String bucket;

    /**
     * 文件后缀
     */
    private String suffix;

    /**
     * 文件外链
     */
    private String url;
}
