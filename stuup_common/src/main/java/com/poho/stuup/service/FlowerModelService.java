package com.poho.stuup.service;

import com.poho.stuup.model.dto.FlowerDTO;
import com.poho.stuup.model.vo.FlowerVO;

public interface FlowerModelService {

    FlowerVO getFlowerModel();

    FlowerVO setFlowerModel(FlowerDTO flowerDTO);
}
