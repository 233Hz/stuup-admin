package com.poho.stuup.service;

import com.poho.common.custom.ResponseModel;
import com.poho.stuup.model.dto.ScoreDetailSearchDTO;
import com.poho.stuup.model.dto.ScoreSearchDTO;
import com.poho.stuup.model.dto.StuScoreSearchDTO;
import com.poho.stuup.model.dto.StuScoreTopSearchDTO;

public interface IScoreService {

    ResponseModel findScorePageResult(ScoreSearchDTO searchDTO);

    ResponseModel getStuScoreTopList(StuScoreTopSearchDTO searchDTO);

    ResponseModel getStuScore(StuScoreSearchDTO searchDTO);

    ResponseModel findScoreDetailPageResult(ScoreDetailSearchDTO searchDTO);

    ResponseModel getStuScoreDetailList(StuScoreSearchDTO searchDTO);
}
