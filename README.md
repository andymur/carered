# GitHub Scoring Service

### About

This is an implementation of service, which returns a set of GitHub repositories by provided language.
Repositories must be more fresh (created later) than provided date.

Alongside with that, a score values is calculated and returned for each of them.

The service uses [GitHub API](https://docs.github.com/en/rest/search/search?apiVersion=2022-11-28#search-repositories) in order to get information about repositories.

### Scoring calculations

Current scoring calculator uses logic as below with rounding to the integer

```
    ln(number_of_stars + number_of_forks - number_of_days_passed_since_last_update)
```

If and only if

```
number_of_stars + number_of_forks - number_of_days_passed_since_last_update
```

is greater than one, otherwise score is calculated as zero.

#### Examples of scoring calculation

Let's assume a repository has 10 forks, 20 stars and the last update date was 5 days before today.

Then the score is calculated as below

```
round(ln(10 + 20 - 5)) = round(ln(25)) = round(3.21) = 3
```
Another example is when a repository has 0 forks, 2 stars and the last update date was 10 days before today.

Then the score is 0, because the partial results below is less than one:

```
0 + 2 - 10 = -8
```

Please be aware, that by design, it is easy to add more calculators and use them.
An option of adding more types of calculations is described below.

### API description

The service has only one GET endpoint

```
    GET /api/repositories?language=<language>&created_start=<YYYY-MM-DD ISO DATE>&page=<page number>&page_size=<page size number>
```

and returns a payload like

```
{
  "totalCount": int,
  "repositories": [
    {
        "name": string,
        "url": string,
        "score": long
    }
  ]
}
```

#### Client request example

The service can be used by cURL client as per example below

```
curl -L "<service host>/api/repositories?language=Java&created_start=2025-11-01&per_page=100&page=1"
```

#### Output payload examples

One example of the payload you can find below

```json
  {
  "totalCount": 1,
  "repositories": [
    {
      "name": "wuyouzhuguli/SpringAll",
      "url": "https://github.com/wuyouzhuguli/SpringAll",
      "score": 11
    }
  ]
}
```

#### Parameter input values

Input parameter values are restricted, please what is supported and what is not

- language parameter must have value from a specific list, which of course can be adjusted
- created_start parameter must be a ISO date (YYYY-MM-DD format)
- per_page & page the requested page must contain items that are in a scope of first 1K repositories and for some implementations page_size value is also restricted, see below

### Technical parts

Technically this service is implemented as Spring Boot Rest Application which provides REST API endpoint described above.

It can be decomposed in the main components from the list below

- RepoScoreController which handles REST endpoint
- ScoreService which gets GitHub results and converts them to the output. It has two implementations, see below
- GitHubService is responsible in talking to GitHub API
- RepositoryScoreMapper is a component which handles conversion from GitHub API response to the service response
- ScoreCalculator is an interface (there is only one implementation currently) of the scoring calculator
- RepositoryScoreCache is a cache which holds results, used only with a certain implementation of ScoreService

#### Flavours of service implementations

There are two implementations internally, how this API serves the user.

The difference between them, that the first one (RepositoryScoreService) always calls GitHub API.
The second one (RepositoryCachedScoreService) caches the results, based on input values like language, date and page number and doesn't call GitHub API if cached version is available.

The second version has better performance, but has some limitations, described below.

Let's depict sequence of calls for the first implementation below.

```
    RepoScoreController --> RepositoryScoreService --> GitHubService --> RepositoryScoreService --> RepositoryScoreMapper --> RepositoryScoreService --> RepoScoreController
```

Let's depict sequence of calls for the second implementation below in case the results is in cache.
If it is not, then the call chain is similar to the one above.

```
    RepoScoreController --> RepositoryCachedScoreService --> RepositoryScoreCache --> RepositoryCachedScoreService --> RepoScoreController
```

One can switch between implementations by assigning proper value to the Spring property

```
carered.servicetype: "cached" | "simple"
```

Cached version is used now.

#### Handling errors and exceptional situations

There are some server & client related exceptions, all located in a error package.
RestExceptionHandler is located there as well.

#### Adding and using different score calculators

As it was already mentioned, currently there is only one implementation of the ScoreCalculator, namely LnScoreCalculator.
If we want to calculate score differently, we need to add another implementation.

ScoreCalculator interface uses a set of features and their values to calculate the score, hence it is decoupled from the other logic.

### Limitations & Problems

For the sake of simplicity there are a list of unsolved potential problems and limitations, let's describe them below.

- Cached version of service has a page size limitation (only 100 items per page)
- Cached version of service has no eviction mechanism and cache resides in JVM heap
- Cached version of service has no TTL, meaning once cached value will be returned until service restart, which strictly speaking is not wanted behaviour
- Language parameter is limited to the configured languages, client must be aware of it
- No sorting is supported by API
- Items are limited to the first 1K of repositories
- Feign library can be used for requesting GitHub API to make code more declarative and adding new features like retries
- OpenAPI generator or similar can be used to describe API contract and help to build a client for it

Of course there are solutions for any of the problems stated above (E.g., for cached version we could use standalone cache like Redis).

### How to use

#### Test it

In order to test the service, you can run the tests using a command below

```
./gradlew test
```

#### Run it

You can either run it from the repository, using Gradle

```
./gradlew bootRun
```

Or as a docker container, with the steps below

```
./gradlew bootJar
docker build -t carered .
docker run -p 8080:8080 carered
```