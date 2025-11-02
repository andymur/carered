package com.andymur.carered.controller;

import com.andymur.carered.model.RepositoryScore;
import com.andymur.carered.model.RepositoryScoreResponse;
import com.andymur.carered.service.ScoreService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RepoScoreController.class)
class RepoScoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

        mockMvc.perform(
                        get("/api/repositories")
                                .param("language", "Java")
                                .param("created_before", "2025-01-01")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(mockResponse)));
    }
}

