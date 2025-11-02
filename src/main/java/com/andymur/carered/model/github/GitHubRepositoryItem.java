package com.andymur.carered.model.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepositoryItem {

    @JsonProperty("language")
    private String language;

    @JsonProperty("full_name")
    private String name;

    @JsonProperty("stargazers_count")
    private int stars;

    @JsonProperty("forks_count")
    private int forksCount;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonProperty("html_url")
    private String url;

    public static GitHubRepositoryItem of(
            String language,
            String name,
            int stars,
            int forksCount,
            Instant updatedAt,
            String url
    ) {
        GitHubRepositoryItem item = new GitHubRepositoryItem();
        item.stars = stars;
        item.forksCount = forksCount;
        item.updatedAt = updatedAt;
        item.language = language;
        item.name = name;
        item.url = url;
        return item;
    }

    public String getName() {
        return name;
    }

    public int getStars() {
        return stars;
    }

    public String getLanguage() {
        return language;
    }

    public int getForksCount() {
        return forksCount;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "RepositoryItem{" +
                "language='" + language + '\'' +
                ", name='" + name + '\'' +
                ", stars=" + stars +
                ", forksCount=" + forksCount +
                ", updatedAt=" + updatedAt +
                ", url='" + url + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        GitHubRepositoryItem that = (GitHubRepositoryItem) o;
        return url.equals(that.url);
    }

    @Override
    public int hashCode() {
        return url.hashCode();
    }
}
