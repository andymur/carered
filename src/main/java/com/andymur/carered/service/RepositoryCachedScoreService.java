package com.andymur.carered.service;

import com.andymur.carered.component.RepositoryScoreCache;
import com.andymur.carered.error.IncorrectPageSizeException;
import com.andymur.carered.model.RepositoryScoreResponse;
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
            throw new IncorrectPageSizeException(pageSize, RepositoryScoreCache.PAGE_SIZE);
        }

        return repositoryScoreCache.readScoreValues(language, createdStart, page);
    }
}
