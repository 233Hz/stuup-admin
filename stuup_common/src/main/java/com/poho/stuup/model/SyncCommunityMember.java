package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@Builder
@AllArgsConstructor
@TableName("t_sync_community_member")
public class SyncCommunityMember implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer syncInfoId; //同步表记录id

    private Integer memberId; //社团成员id

    private String termName; // 学期名称

    private String communityName; //社团名称

    private String stuNo; //学号

    private String stuName; //姓名

    private Integer state; // 状态 0 未处理 1处理成功  2处理失败

    private String memo;

    private Date createTime;

    private Date updateTime;
    
}
