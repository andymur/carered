package com.andymur.carered.service.github;

import com.andymur.carered.model.github.GitHubSearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class GitHubService {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String GITHUB_API_URL = "https://api.github.com/search/repositories";

    private static final OkHttpClient client = new OkHttpClient();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public GitHubSearchResponse fetchGitHubRepositories(
            String language,
            LocalDate createdStart,
            int page,
            int pageSize
    ) {
        try {
            return fetchGitHub(createHttpRequest(language, formatter.format(createdStart), page, pageSize));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request createHttpRequest(String language, String createdStart, int page, int pageSize) {
        String query = String.format("language:%s created:>=%s", language, createdStart);
        HttpUrl url = HttpUrl.parse(GITHUB_API_URL).newBuilder()
                .addQueryParameter("q", query)
                .addQueryParameter("per_page", String.valueOf(pageSize))
                .addQueryParameter("page", String.valueOf(page))
                .build();

        return new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/vnd.github+json")
                .addHeader("X-GitHub-Api-Version", "2022-11-28")
                .build();
    }

    private GitHubSearchResponse fetchGitHub(
            Request request
    ) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            String body = response.body().string();
            return objectMapper.readValue(body, GitHubSearchResponse.class);
        }
    }
}
