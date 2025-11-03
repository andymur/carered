package com.andymur.carered.component;

import com.andymur.carered.model.RepositoryScoreResponse;
import com.andymur.carered.service.github.GitHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RepositoryScoreCache {
    public static final int PAGE_SIZE = 100;

    private final Map<RepositoryScoreKey, RepositoryScoreResponse> cache = new ConcurrentHashMap<>();
    private final GitHubService gitHubService;
    private final RepositoryScoreMapper repositoryScoreMapper;

    @Autowired
    public RepositoryScoreCache(
            GitHubService gitHubService,
            RepositoryScoreMapper repositoryScoreMapper
    ) {
        this.gitHubService = gitHubService;
        this.repositoryScoreMapper = repositoryScoreMapper;
    }

    public RepositoryScoreResponse readScoreValues(
            String language,
            LocalDate date,
            int page
    ) {
        return cache.computeIfAbsent(
                RepositoryScoreKey.of(language, date.atStartOfDay().toEpochSecond(ZoneOffset.UTC), page),
                k -> repositoryScoreMapper.mapGitHubResponse(
                        gitHubService.fetchGitHubRepositories(language, date, page, PAGE_SIZE)
                )
        );
    }

    static class RepositoryScoreKey {
        private final String language;
        private final long dateTimeStamp;
        private final int page;

        private RepositoryScoreKey(
                String language,
                long dateTimeStamp,
                int page
        ) {
            this.language = language;
            this.dateTimeStamp = dateTimeStamp;
            this.page = page;
        }

        static RepositoryScoreKey of(
                String language,
                long dateTimeStamp,
                int page
        ) {
            return new RepositoryScoreKey(language, dateTimeStamp, page);
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;

            RepositoryScoreKey that = (RepositoryScoreKey) o;
            return dateTimeStamp == that.dateTimeStamp && page == that.page && language.equals(that.language);
        }

        @Override
        public int hashCode() {
            int result = language.hashCode();
            result = 31 * result + Long.hashCode(dateTimeStamp);
            result = 31 * result + page;
            return result;
        }
    }
}
