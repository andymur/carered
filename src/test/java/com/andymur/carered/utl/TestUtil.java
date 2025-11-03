package com.andymur.carered.utl;

import com.andymur.carered.model.github.GitHubRepositoryItem;
import com.andymur.carered.model.github.GitHubSearchResponse;

import java.time.Instant;
import java.util.List;

public class TestUtil {
    public static GitHubSearchResponse createGitHubSearchResponse(
            int totalCount,
            List<GitHubRepositoryItem> items
    ) {
        GitHubSearchResponse gitHubSearchResponse = new GitHubSearchResponse();
        gitHubSearchResponse.setTotalCount(totalCount);
        gitHubSearchResponse.setItems(items);
        return gitHubSearchResponse;
    }

    public static GitHubRepositoryItem createGitHubRepositoryItem(
            String name,
            String language,
            String url,
            int stars,
            int forksCount,
            Instant createdAt,
            Instant updatedAt
    ) {
        GitHubRepositoryItem item = new GitHubRepositoryItem();
        item.setName(name);
        item.setLanguage(language);
        item.setUrl(url);
        item.setStars(stars);
        item.setForksCount(forksCount);
        item.setUpdatedAt(updatedAt);
        item.setCreatedAt(createdAt);
        return item;
    }
}
