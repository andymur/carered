package com.andymur.carered.controller;

import com.andymur.carered.model.Language;
import com.andymur.carered.model.RepositoryScoreResponse;
import com.andymur.carered.service.ScoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
public class RepoScoreController {

    private static final Logger log = LoggerFactory.getLogger(RepoScoreController.class);
    private final ScoreService scoreService;

    @Autowired
    public RepoScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/repositories")
    public ResponseEntity<RepositoryScoreResponse> getRepositories(
            @RequestParam Language language,
            @RequestParam(name = "created_before")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdBefore,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "page_size", defaultValue = "100", required = false) Integer pageSize
    ) {
        // Add 1000 items limitation check
        RepositoryScoreResponse response = scoreService.fetchRepositoriesScores(
                language.name(),
                createdBefore,
                page,
                pageSize);
        return ResponseEntity.ok().body(response);
    }
}
