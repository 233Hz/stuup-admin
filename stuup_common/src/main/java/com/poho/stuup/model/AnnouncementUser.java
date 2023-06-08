package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 用户公告表
 * </p>
 *
 * @author BUNGA
 * @since 2023-06-08
 */
@Getter
@Setter
@TableName("t_announcement_user")
public class AnnouncementUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 公告id
     */
    private Long announcementId;

    /**
     * 用户id
     */
    private Long userId;


}
