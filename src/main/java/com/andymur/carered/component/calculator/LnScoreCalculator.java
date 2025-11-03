package com.andymur.carered.component.calculator;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class LnScoreCalculator implements ScoreCalculator {

    @Override
    public long calculate(Map<String, Long> featureMap) {
        if (featureMap == null || featureMap.isEmpty()) {
            return -1;
        }
        long preliminaryScore = featureMap.get(STARS_COUNT_FEATURE)
                + featureMap.get(FORK_COUNT_FEATURE)
                - featureMap.get(DAYS_PASSED_FEATURE);
        return preliminaryScore < 1 ? 0L : Math.round(Math.log((double) preliminaryScore));
    }
}
