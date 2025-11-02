package com.andymur.carered.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubSearchResponse {

    @JsonProperty("total_count")
    private int totalCount;

    @JsonProperty("items")
    private List<GitHubRepositoryItem> items;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<GitHubRepositoryItem> getItems() {
        return items;
    }

    public void setItems(List<GitHubRepositoryItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "GitHubSearchResponse{" +
                "totalCount=" + totalCount +
                ", items=" + items +
                '}';
    }
}
