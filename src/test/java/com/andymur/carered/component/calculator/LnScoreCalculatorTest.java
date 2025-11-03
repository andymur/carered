package com.andymur.carered.component.calculator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.andymur.carered.component.calculator.ScoreCalculator.*;

class LnScoreCalculatorTest {

    @Test
    void shouldCalculateLnScore() {
        Map<String, Long> featureMap = Map.of(
                STARS_COUNT_FEATURE, 10L,
                FORK_COUNT_FEATURE, 10L,
                DAYS_PASSED_FEATURE, 10L
        );
        ScoreCalculator calculator = new LnScoreCalculator();

        Assertions.assertEquals(2L, calculator.calculate(featureMap));
    }

    @Test
    void shouldReturnZeroScoreIfPreliminaryScoreIsNegative() {
        Map<String, Long> featureMap = Map.of(
                STARS_COUNT_FEATURE, 0L,
                FORK_COUNT_FEATURE, 0L,
                DAYS_PASSED_FEATURE, 10L
        );
        ScoreCalculator calculator = new LnScoreCalculator();

        Assertions.assertEquals(0L, calculator.calculate(featureMap));
    }
}