package com.andymur.carered.service;

import com.andymur.carered.model.RepositoryScoreResponse;

import java.time.LocalDate;

public interface ScoreService {
    /**
     * Gets repository score response
     *
     * @param language     of the repository
     * @param createdStart date, repository is created on this date or later
     * @param page         number of the response
     * @param pageSize     of the response
     * @return response that contains total number of found repos for the provided language and created later than provided date
     * and a page with set of them
     */
    RepositoryScoreResponse fetchRepositoriesScores(String language, LocalDate createdStart, int page, int pageSize);
}
