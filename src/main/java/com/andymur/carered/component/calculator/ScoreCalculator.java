package com.andymur.carered.component.calculator;

import java.util.Map;

public interface ScoreCalculator {

    String FORK_COUNT_FEATURE = "fork_count";
    String STARS_COUNT_FEATURE = "stars_count";
    String DAYS_PASSED_FEATURE = "days_passed";

    long calculate(Map<String, Long> featureMap);
}
