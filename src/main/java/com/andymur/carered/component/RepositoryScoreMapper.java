package com.andymur.carered.component;

import com.andymur.carered.component.calculator.ScoreCalculator;
import com.andymur.carered.model.RepositoryScore;
import com.andymur.carered.model.RepositoryScoreResponse;
import com.andymur.carered.model.github.GitHubSearchResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.andymur.carered.component.calculator.ScoreCalculator.*;
import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class RepositoryScoreMapper {

    private final ScoreCalculator scoreCalculator;

    @Autowired
    public RepositoryScoreMapper(ScoreCalculator scoreCalculator) {
        this.scoreCalculator = scoreCalculator;
    }

    public RepositoryScoreResponse mapGitHubResponse(GitHubSearchResponse gitHubSearchResponse) {
        Set<RepositoryScore> items = gitHubSearchResponse.getItems().stream().map(
                gitHubRepositoryItem -> RepositoryScore.of(gitHubRepositoryItem.getName(), gitHubRepositoryItem.getUrl(),
                        scoreCalculator.calculate(Map.of(
                                FORK_COUNT_FEATURE, (long) gitHubRepositoryItem.getForksCount(),
                                STARS_COUNT_FEATURE, (long) gitHubRepositoryItem.getStars(),
                                DAYS_PASSED_FEATURE, DAYS.between(gitHubRepositoryItem.getUpdatedAt(), Instant.now()))
                        ))
        ).collect(Collectors.toSet());
        return RepositoryScoreResponse.of(
                gitHubSearchResponse.getTotalCount(),
                items
        );
    }
}
