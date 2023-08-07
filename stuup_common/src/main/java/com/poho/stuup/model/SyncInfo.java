package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@TableName("t_sync_info")
public class SyncInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    public SyncInfo (){}

    public SyncInfo (String businessCode, String businessDescription, String businessKey){
        this.businessCode = businessCode;
        this.businessDescription = businessDescription;
        this.businessKey = businessKey;
    }

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String businessCode;

    private String businessDescription;

    private String businessKey;

    private Integer state; // 状态 1 同步中 2同步成功 3同步失败

    private String memo;

    private Date createTime;

    private Date updateTime;

}
