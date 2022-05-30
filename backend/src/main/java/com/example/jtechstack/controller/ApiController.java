package com.example.jtechstack.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.jtechstack.entity.Dependency;
import com.example.jtechstack.entity.MavenRepo;
import com.example.jtechstack.entity.Repository;
import com.example.jtechstack.service.DependencyService;
import com.example.jtechstack.service.RepositoryService;
import com.example.jtechstack.utils.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private final Logger logger = LogManager.getLogger(this.getClass().getName());

    private final RepositoryService repositoryService;
    private final DependencyService dependencyService;

    public ApiController(RepositoryService repositoryService, DependencyService dependencyService) {
        this.repositoryService = repositoryService;
        this.dependencyService = dependencyService;
    }

    @GetMapping("/repositories")
    public List<Repository> getRepositories(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "limit", required = false, defaultValue = "30") int limit
    ) {
        QueryWrapper<Repository> qw = new QueryWrapper<Repository>()
                .orderByDesc("stars")
                .last(String.format("limit %d offset %d", limit, offset));
        return repositoryService.list(qw);
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
}