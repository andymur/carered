package com.andymur.carered.service;

import com.andymur.carered.component.RepositoryScoreMapper;
import com.andymur.carered.model.RepositoryScoreResponse;
import com.andymur.carered.model.github.GitHubSearchResponse;
import com.andymur.carered.service.github.GitHubService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@ConditionalOnProperty(
        name = "carered.servicetype",
        havingValue = "simple"
)
public class RepositoryScoreService implements ScoreService {
    private static final Logger log = LoggerFactory.getLogger(RepositoryCachedScoreService.class);

    private final GitHubService gitHubService;
    private final RepositoryScoreMapper scoreMapper;

    @Autowired
    public RepositoryScoreService(
            GitHubService gitHubService,
            RepositoryScoreMapper scoreMapper
    ) {
        this.gitHubService = gitHubService;
        this.scoreMapper = scoreMapper;
    }

    public RepositoryScoreResponse fetchRepositoriesScores(
            String language,
            LocalDate createdStart,
            int page,
            int pageSize
    ) {
        log.info("fetchRepositoriesScores; fetching repository score using language: {}, date: {} and page number: {}",
                language, createdStart, page);
        GitHubSearchResponse gitHubSearchResponse = gitHubService.fetchGitHubRepositories(language, createdStart, page, pageSize);
        return scoreMapper.mapGitHubResponse(gitHubSearchResponse);
    }
}
