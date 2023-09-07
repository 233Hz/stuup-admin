package com.poho.stuup.model.dto;

import com.poho.stuup.constant.ValidationGroups;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class GrowthAuditDTO {

    @NotNull(message = "请选择要审核的人", groups = {ValidationGroups.Audit.Single.Pass.class, ValidationGroups.Audit.Single.NoPass.class})
    private Long id;

    @NotNull(message = "请选中要审核的人", groups = {ValidationGroups.Audit.Single.NoPass.class, ValidationGroups.Audit.Batch.NoPass.class})
    private String reason;

    @NotNull(message = "请选择要审核的人", groups = {ValidationGroups.Audit.Batch.Pass.class, ValidationGroups.Audit.Batch.NoPass.class})
    private List<Long> ids;
}
