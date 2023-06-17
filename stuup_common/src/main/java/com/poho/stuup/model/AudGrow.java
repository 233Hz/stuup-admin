package com.poho.stuup.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.poho.stuup.constant.ValidationGroups;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 成长项目审核记录
 * </p>
 *
 * @author BUNGA
 * @since 2023-05-29
 */
@Getter
@Setter
@TableName("t_aud_grow")
public class AudGrow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空", groups = ValidationGroups.Update.class)
    private Long id;

    /**
     * 项目id
     */
    @NotNull(message = "申请项目不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private Long growId;

    /**
     * 审核状态（0.待提交 1.待审核 2.审核通过 3.审核失败 4.退回）
     */
    private Integer state;

    /**
     * 申请人
     */
    private Long applicant;

    /**
     * 审核人
     */
    private Long auditor;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 申请原因
     */
    @NotBlank(message = "申请原因不能为空", groups = {ValidationGroups.Insert.class, ValidationGroups.Update.class})
    private String reason;

    /**
     * 附件id
     */
    private String fileIds;


}
