package com.poho.stuup.growth;

import com.poho.stuup.model.GrowthItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RecImportParams {


    private Long userId;

    private Long yearId;

    private Long semesterId;

    private GrowthItem growthItem;

}
