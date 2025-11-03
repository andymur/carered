package com.andymur.carered.service;

import com.andymur.carered.component.RepositoryScoreCache;
import com.andymur.carered.error.IncorrectPageSizeException;
import com.andymur.carered.model.RepositoryScoreResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@ConditionalOnProperty(
        name = "carered.servicetype",
        havingValue = "cached"
)
public class RepositoryCachedScoreService implements ScoreService {
    private static final Logger log = LoggerFactory.getLogger(RepositoryCachedScoreService.class);
    private final RepositoryScoreCache repositoryScoreCache;

    @Autowired
    public RepositoryCachedScoreService(RepositoryScoreCache repositoryScoreCache) {
        this.repositoryScoreCache = repositoryScoreCache;
    }

    @Override
    public RepositoryScoreResponse fetchRepositoriesScores(
            String language,
            LocalDate createdStart,
            int page,
            int pageSize
    ) {

        if (pageSize != RepositoryScoreCache.PAGE_SIZE) {
            log.error("Page size of value {} is not supported", pageSize);
            throw new IncorrectPageSizeException(pageSize, RepositoryScoreCache.PAGE_SIZE);
        }
        log.info("fetchRepositoriesScores; reading repository score from cache using language: {}, date: {} and page number: {}",
                language, createdStart, page);
        return repositoryScoreCache.readScoreValues(language, createdStart, page);
    }
}
