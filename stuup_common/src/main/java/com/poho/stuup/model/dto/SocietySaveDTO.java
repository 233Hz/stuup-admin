package com.poho.stuup.model.dto;


import com.poho.stuup.model.GrowthItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
public class SocietySaveDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer communityMemberId;

    private Long batchCode;

    private Long currYearId;

    private Long currSemesterId;

    private GrowthItem growthItem;

    private Long stuId;

    private String communityName;


}
