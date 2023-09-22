package com.poho.stuup.service;

import java.math.BigDecimal;

public interface TestService {

    void saveResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, BigDecimal score);

    void saveResult(Long studentId, Long growthItemId, Long yearId, Long semesterId, BigDecimal score, BigDecimal scoreUpperLimit, Integer count);
}
