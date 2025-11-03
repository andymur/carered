package com.andymur.carered.controller;

import com.andymur.carered.error.IncorrectPageException;
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
    public static final int AVAILABLE_ITEMS_LIMIT = 1000;

    private static final Logger log = LoggerFactory.getLogger(RepoScoreController.class);
    private final ScoreService scoreService;

    @Autowired
    public RepoScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/repositories")
    public ResponseEntity<RepositoryScoreResponse> getRepositories(
            @RequestParam Language language,
            @RequestParam(name = "created_start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdStart,
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "page_size", defaultValue = "100", required = false) Integer pageSize
    ) {
        log.info("getRepositories; get repositories with scores by parameters: language = {}, created_start = {}, page = {}, page_size = {}",
                language, createdStart, page, pageSize);
        checkPageAvailability(page, pageSize);
        RepositoryScoreResponse response = scoreService.fetchRepositoriesScores(
                language.name(),
                createdStart,
                page,
                pageSize);
        log.info("getRepositories; returning repositories with scores response: total repository count = {} repository count on page = {}",
                response.getTotalCount(), response.getRepositories().size());
        return ResponseEntity.ok().body(response);
    }

    private void checkPageAvailability(int page, int pageSize) {
        if (page * pageSize - 1 >= AVAILABLE_ITEMS_LIMIT) {
            log.error("Page {} of size {} is out of available items", page, pageSize);
            throw new IncorrectPageException(page, pageSize);
        }
    }
}
