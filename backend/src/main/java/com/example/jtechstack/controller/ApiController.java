package com.example.jtechstack.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jtechstack.entity.*;
import com.example.jtechstack.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    private final RepositoryService repositoryService;
    private final DependencyService dependencyService;
    private final ContributorService contributorService;
    private final UserService userService;
    private final RepoTopicService repoTopicService;
    private final MavenRepoService mavenRepoService;

    public ApiController(RepositoryService repositoryService, DependencyService dependencyService, ContributorService contributorService, UserService userService, RepoTopicService repoTopicService, MavenRepoService mavenRepoService) {
        this.repositoryService = repositoryService;
        this.dependencyService = dependencyService;
        this.contributorService = contributorService;
        this.userService = userService;
        this.repoTopicService = repoTopicService;
        this.mavenRepoService = mavenRepoService;
    }

    @GetMapping("/repositories")
    public List<Repository> getRepositories(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
            @RequestParam(value = "sort", required = false, defaultValue = "stars") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order,
            @RequestParam(value = "management", required = false, defaultValue = "") String management
    ) {
        QueryWrapper<Repository> qw = new QueryWrapper<>();
        qw.orderBy(true, order.equals("asc"), sort);

        if (limit < 0) {
            qw.last(String.format("offset %d", offset));
        } else {
            qw.last(String.format("limit %d offset %d", limit, offset));
        }

        if (!management.isEmpty()) {
            qw.eq("management", management);
        }

        return repositoryService.list(qw);
    }

    @GetMapping("/maven-packages")
    public List<MavenRepo> getMavenPackages(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
            @RequestParam(value = "sort", required = false, defaultValue = "used_by") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order
    ) {
        QueryWrapper<MavenRepo> qw = new QueryWrapper<>();
        qw.orderBy(true, order.equals("asc"), sort);

        if (limit < 0) {
            qw.last(String.format("offset %d", offset));
        } else {
            qw.last(String.format("limit %d offset %d", limit, offset));
        }

        return mavenRepoService.list(qw);
    }

    @GetMapping("/topics")
    public List<String> getTopics() {
        QueryWrapper<RepoTopic> qw = new QueryWrapper<>();
        qw.select("distinct topic_str");
        return repoTopicService.listMaps(qw).stream()
                .map(o -> (String) o.get("topic_str"))
                .collect(Collectors.toList());
    }

    @GetMapping("/repository/{repo_id}/dependencies")
    public List<Map<String, Object>> getRepoDependencies(
            @PathVariable(value = "repo_id") int repoId
    ) {
        return dependencyService.getRepoDependencies(repoId).stream()
                .map(p -> new HashMap<String, Object>() {{
                    this.put("dependency", p.getLeft());
                    this.put("mavenRepo", p.getRight());
                }})
                .collect(Collectors.toList());
    }

    @GetMapping("/maven-package/{mvn_repo_id}/usages")
    public List<Repository> getMavenPackageUsages(
            @PathVariable(value = "mvn_repo_id") String mvnRepoId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
            @RequestParam(value = "sort", required = false, defaultValue = "stars") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order
    ) {
        List<Dependency> dependencyList = dependencyService.list(new QueryWrapper<Dependency>()
                .eq("mvn_repo_id", mvnRepoId));
        List<Integer> repoIdList = dependencyList.stream()
                .map(Dependency::getRepoId)
                .collect(Collectors.toList());

        if (repoIdList.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<Repository> qw = new QueryWrapper<>();
        qw.in(true, "id", repoIdList);
        qw.orderBy(true, order.equals("asc"), sort);

        if (limit < 0) {
            qw.last(String.format("offset %d", offset));
        } else {
            qw.last(String.format("limit %d offset %d", limit, offset));
        }

        return repositoryService.list(qw);
    }

    @GetMapping("/repository/{repo_id}/contributors")
    public List<Map<String, Object>> getContributors(
            @PathVariable(value = "repo_id") int repoId
    ) {
        List<Contributor> contributors = contributorService.list(new QueryWrapper<Contributor>()
                .eq("repo_id", repoId));
        List<Integer> userIdList = contributors.stream()
                .map(Contributor::getUserId)
                .collect(Collectors.toList());
        Map<Integer, Integer> userContributionMap = contributors.stream()
                .collect(Collectors.toMap(Contributor::getUserId, Contributor::getContributions));

        if (userIdList.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.in(true, "id", userIdList);

        List<Map<String, Object>> users = userService.listMaps(qw);
        users.parallelStream()
                .forEach(userMap -> userMap.put("contributions", userContributionMap.get((int) userMap.get("id"))));
        users.sort((o1, o2) -> (int) o2.get("contributions") - (int) o1.get("contributions"));
        return users;
    }

    @GetMapping("/user/{user_id}/repositories")
    public List<Repository> getUserRepositories(
            @PathVariable(value = "user_id") int userId,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
            @RequestParam(value = "sort", required = false, defaultValue = "stars") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order
    ) {
        QueryWrapper<Repository> qw = new QueryWrapper<>();
        qw.in(true, "owner_id", userId);
        qw.orderBy(true, order.equals("asc"), sort);

        if (limit < 0) {
            qw.last(String.format("offset %d", offset));
        } else {
            qw.last(String.format("limit %d offset %d", limit, offset));
        }

        return repositoryService.list(qw);
    }


    @GetMapping("/topic/{topic_str}/repositories")
    public List<Repository> getTopicRepositories(
            @PathVariable(value = "topic_str") String topicStr,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit,
            @RequestParam(value = "sort", required = false, defaultValue = "stars") String sort,
            @RequestParam(value = "order", required = false, defaultValue = "desc") String order
    ) {
        List<RepoTopic> repoTopicList = repoTopicService.list(new QueryWrapper<RepoTopic>()
                .eq("topic_str", topicStr));
        List<Integer> repoIdList = repoTopicList.stream()
                .map(RepoTopic::getRepoId)
                .collect(Collectors.toList());

        if (repoIdList.isEmpty()) {
            return new ArrayList<>();
        }

        QueryWrapper<Repository> qw = new QueryWrapper<>();
        qw.in(true, "id", repoIdList);
        qw.orderBy(true, order.equals("asc"), sort);

        if (limit < 0) {
            qw.last(String.format("offset %d", offset));
        } else {
            qw.last(String.format("limit %d offset %d", limit, offset));
        }

        return repositoryService.list(qw);
    }
}
