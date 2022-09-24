package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.ScoreDetailSearchDTO;
import com.poho.stuup.model.dto.ScoreSearchDTO;

public interface IScoreService {

    ResponseModel findScorePageResult(ScoreSearchDTO searchDTO);

    ResponseModel findScoreDetailPageResult(ScoreDetailSearchDTO searchDTO);
}
