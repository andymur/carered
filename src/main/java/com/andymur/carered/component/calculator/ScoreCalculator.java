package com.andymur.carered.component.calculator;

import java.util.Map;

public interface ScoreCalculator {

    String FORK_COUNT_FEATURE = "fork_count";
    String STARS_COUNT_FEATURE = "stars_count";
    String DAYS_PASSED_FEATURE = "days_passed";

    /**
     * Calculates a score, based on provided features
     *
     * @param featureMap contains all needed feature values to calculate a score
     * @return non-negative score number
     */
    long calculate(Map<String, Long> featureMap);
}
