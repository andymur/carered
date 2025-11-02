package com.andymur.carered.model;

public class RepositoryScore {

    private String name;
    private String url;
    private long score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public static RepositoryScore of(
            String name, String url,
            long score
    ) {
        RepositoryScore repositoryScore = new RepositoryScore();
        repositoryScore.setName(name);
        repositoryScore.setUrl(url);
        repositoryScore.setScore(score);
        return repositoryScore;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryScore that = (RepositoryScore) o;
        return score == that.score && name.equals(that.name) && url.equals(that.url);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + url.hashCode();
        result = 31 * result + Long.hashCode(score);
        return result;
    }

    @Override
    public String toString() {
        return "Repository{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                ", score=" + score +
                '}';
    }
}
