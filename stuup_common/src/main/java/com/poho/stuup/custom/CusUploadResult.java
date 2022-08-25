package com.poho.stuup.custom;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author wupeng
 */
public class CusUploadResult {
    @ApiModelProperty("文件相对路径")
    private String path;
    @ApiModelProperty("文件名称")
    private String title;
    @ApiModelProperty("文件URL")
    private String url;

    public CusUploadResult(String url, String path, String title) {
        this.path = path;
        this.title = title;
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
