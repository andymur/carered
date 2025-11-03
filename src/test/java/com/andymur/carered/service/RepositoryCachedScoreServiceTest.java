package com.andymur.carered.service;

import com.andymur.carered.component.RepositoryScoreCache;
import com.andymur.carered.component.RepositoryScoreMapper;
import com.andymur.carered.component.calculator.LnScoreCalculator;
import com.andymur.carered.model.RepositoryScore;
import com.andymur.carered.model.RepositoryScoreResponse;
import com.andymur.carered.service.github.GitHubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.andymur.carered.utl.TestUtil.createGitHubRepositoryItem;
import static com.andymur.carered.utl.TestUtil.createGitHubSearchResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RepositoryCachedScoreServiceTest {
    private RepositoryCachedScoreService scoreService;

    @Mock
    private GitHubService gitHubService;

    @BeforeEach
    void setUp() {
        scoreService = new RepositoryCachedScoreService(
                new RepositoryScoreCache(
                        gitHubService, new RepositoryScoreMapper(new LnScoreCalculator())
                )
        );
    }

    @Test
    void shouldCreateScoreResponseWhenGetNormalResponseFromGitHub() {
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        LocalDate createdStart = createdAt.minus(10, ChronoUnit.DAYS).atZone(ZoneId.systemDefault())
                .toLocalDate();

        Mockito.when(gitHubService.fetchGitHubRepositories("Java", createdStart, 0, 100)).thenReturn(
                createGitHubSearchResponse(100, List.of(
                        createGitHubRepositoryItem("test",
                                "Java",
                                "http://test.test",
                                10,
                                20,
                                updatedAt,
                                createdAt
                        )
                ))
        );

        scoreService.fetchRepositoriesScores("Java", createdStart, 0, 100);
        RepositoryScoreResponse actualResponse = scoreService.fetchRepositoriesScores("Java", createdStart, 0, 100);

        Mockito.verify(gitHubService, Mockito.times(1))
                .fetchGitHubRepositories("Java", createdStart, 0, 100);

        assertEquals(100, actualResponse.getTotalCount());
        assertEquals(1, actualResponse.getRepositories().size());
        assertEquals(
                RepositoryScore.of("test", "http://test.test", 3), // round(ln (10 + 20 - 10))
                actualResponse.getRepositories().iterator().next()
        );
    }

}