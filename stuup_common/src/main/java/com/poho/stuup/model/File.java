package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件管理表
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-16
 */
@Getter
@Setter
@TableName("t_file")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 文件存储名
     */
    @NotBlank(message = "文件存储名不能为空")
    private String storageName;

    /**
     * 文件原始名
     */
    @NotBlank(message = "文件原始名不能为空")
    private String originalName;

    /**
     * 文件所在的桶名称
     */
    @NotBlank(message = "文件所在的桶名称不能为空")
    private String bucket;

    /**
     * 文件后缀
     */
    @NotBlank(message = "文件后缀不能为空")
    private String suffix;

    /**
     * 上传用户
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;


}
