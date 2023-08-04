package com.poho.stuup.event;

import com.poho.stuup.model.dto.CalculateFailDTO;
import org.springframework.context.ApplicationEvent;

/**
 * @author BUNGA
 * @description: TODO
 * @date 2023/8/3 17:42
 */
public class CalculateFailEvent extends ApplicationEvent {

    private final CalculateFailDTO calculateFailDTO;

    public CalculateFailEvent(CalculateFailDTO calculateFailDTO) {
        super(calculateFailDTO);
        this.calculateFailDTO = calculateFailDTO;
    }

    public CalculateFailDTO getCalculateFailDTO() {
        return calculateFailDTO;
    }
}
