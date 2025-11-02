package com.andymur.carered.model;

import java.util.Set;

public class RepositoryScoreResponse {
    private int totalCount;
    private Set<RepositoryScore> repositories;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Set<RepositoryScore> getRepositories() {
        return repositories;
    }

    public void setRepositories(Set<RepositoryScore> repositories) {
        this.repositories = repositories;
    }

    public static RepositoryScoreResponse of(
            int totalCount,
            Set<RepositoryScore> repositories
    ) {
        RepositoryScoreResponse response = new RepositoryScoreResponse();
        response.setTotalCount(totalCount);
        response.setRepositories(repositories);
        return response;
    }

    @Override
    public String toString() {
        return "RepositoryScoreResponse{" +
                "totalCount=" + totalCount +
                ", repositories=" + repositories +
                '}';
    }
}
