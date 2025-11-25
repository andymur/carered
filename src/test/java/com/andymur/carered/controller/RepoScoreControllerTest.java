package com.andymur.carered.controller;

import com.andymur.carered.model.ErrorResponse;
import com.andymur.carered.model.RepositoryScore;
import com.andymur.carered.model.RepositoryScoreResponse;
import com.andymur.carered.service.ScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = RepoScoreController.class)
class RepoScoreControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ScoreService scoreService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnRepositories() throws Exception {
        RepositoryScoreResponse mockResponse = new RepositoryScoreResponse();
        mockResponse.setTotalCount(5);
        mockResponse.setRepositories(Set.of(new RepositoryScore()));

        when(scoreService.fetchRepositoriesScores(
                Mockito.eq("Java"), any(), anyInt(), anyInt()))
                .thenReturn(mockResponse);

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/repositories")
                        .queryParam("language", "Java")
                        .queryParam("created_start", "2025-01-01")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json(objectMapper.writeValueAsString(mockResponse));
    }

    @Test
    void shouldReturnClientErrorWhenLanguageIsNotSupported() throws Exception {
        ErrorResponse mockResponse = new ErrorResponse("Rust is not supported");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/repositories")
                        .queryParam("language", "Rust")
                        .queryParam("created_start", "2025-01-01")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .json(objectMapper.writeValueAsString(mockResponse));
    }

    @Test
    void shouldReturnClientErrorWhenExceedsSupportedNumberOfItems() throws Exception {
        ErrorResponse mockResponse = new ErrorResponse("Payload on page 11 with page size 100 exceeds supported payload size of 1000 items");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/repositories")
                        .queryParam("language", "Python")
                        .queryParam("created_start", "2025-01-01")
                        .queryParam("page", "11")
                        .queryParam("page_size", "100")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .json(objectMapper.writeValueAsString(mockResponse));
    }

    @Test
    void shouldReturnClientErrorWhenRequiredParameterIsMissing() throws Exception {
        ErrorResponse mockResponse = new ErrorResponse("Missing required parameter: Required query parameter 'language' is not present.");

        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/repositories")
                        .queryParam("created_start", "2025-01-01")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .json(objectMapper.writeValueAsString(mockResponse));
    }

    //TODO: add more tests for edge cases
}

