package com.andymur.carered.service;

import com.andymur.carered.model.RepositoryScoreResponse;

import java.time.LocalDate;

public interface ScoreService {
    RepositoryScoreResponse fetchRepositoriesScores(String language, LocalDate createdBefore, int page, int pageSize);
}
