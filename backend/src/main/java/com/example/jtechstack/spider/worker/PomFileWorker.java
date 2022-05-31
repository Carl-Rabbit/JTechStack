package com.example.jtechstack.spider.worker;


import com.example.jtechstack.entity.Dependency;
import com.example.jtechstack.entity.MavenRepo;
import com.example.jtechstack.entity.Repository;
import com.example.jtechstack.service.DependencyService;
import com.example.jtechstack.service.MavenRepoService;
import com.example.jtechstack.service.RepositoryService;
import com.example.jtechstack.spider.PageWorker;
import com.example.jtechstack.spider.common.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.example.jtechstack.spider.common.SpiderParam.*;

@Component
public class PomFileWorker implements PageWorker {

    private static final Pattern POM_FILE_URL = Pattern.compile("https://raw\\.githubusercontent\\.com/[^/].*/[^/].*/[^/].*/pom\\.xml");

    private static final Pattern PARAM_PATTERN = Pattern.compile("\\$\\{(.*)}");

    private static final Logger logger = LoggerFactory.getLogger(PomFileWorker.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final RepositoryService repositoryService;
    private final DependencyService dependencyService;
    private final MavenRepoService mavenRepoService;

    public PomFileWorker(RepositoryService repositoryService, DependencyService dependencyService, MavenRepoService mavenRepoService) {
        this.repositoryService = repositoryService;
        this.dependencyService = dependencyService;
        this.mavenRepoService = mavenRepoService;
    }

    @Override
    public Pattern getPagePattern() {
        return POM_FILE_URL;
    }

    @Override
    public void process(Page page) {
        logger.info("Start process " + page.getRequest().getUrl());

        int repoId = page.getRequest().getExtra(REPO_ID);

        Document doc = Jsoup.parse(page.getRawText());

        /* read properties */

        Map<String, String> propertyMap = doc
                .select("properties > *")
                .stream()
                .map(child -> new String[]{child.nodeName(), child.text()})
                .collect(Collectors.toMap(p -> p[0], p -> p[1], (p0, p1) -> p1));

        /* read dependency */

        List<Map<String, String>> depMaps = doc
                .select("dependencies > dependency")
                .stream()
                .map(el -> parseDepMap(el, propertyMap))
                .filter(d -> d.containsKey("groupId") && d.containsKey("artifactId"))
                .collect(Collectors.toList());

        /* save result */

        // dependency list
        List<Dependency> dependencyList = getDependencies(repoId, depMaps);
        dependencyService.updateRepoDependencies(repoId, dependencyList);
        logger.info("Save {} dependencies for repo {}", dependencyList.size(), repoId);

        // java version
        String javaVersion = propertyMap.getOrDefault("java.version", "unknown");
        repositoryService.updateById(Repository.builder()
                .id(repoId)
                .management("Maven")
                .javaVersion(javaVersion)
                .build());

        /* add target page */

        for (Map<String, String> depMap : depMaps) {
            String groupId = depMap.get("groupId");
            String artifactId = depMap.get("artifactId");
            if (isMavenRepoOverdue(groupId, artifactId)) {
                String mavenSearchUrl = String.format("https://mvnrepository.com/artifact/%s/%s", groupId, artifactId);
                page.addTargetRequest(RequestUtil.create(mavenSearchUrl)
                        .putExtra(P_USE_CURL, true)
                        .setPriority(PRIORITY_MVN_REPO));
                logger.info("Add target {}", mavenSearchUrl);
            } else {
                logger.info("Maven repo {} {} doesn't need to update", groupId, artifactId);
            }
        }
    }

    private Map<String, String> parseDepMap(Element el, Map<String, String> propertyMap) {
        return el.children()
                .stream()
                .map(child -> {
                    String key = child.nodeName();
                    if (key.matches("(?i)groupId")) {
                        key = "groupId";
                    } else if (key.matches("(?i)artifactId")) {
                        key = "artifactId";
                    }
                    String value = child.text();
                    Matcher m = PARAM_PATTERN.matcher(value);
                    if (m.matches()) {
                        value = propertyMap.getOrDefault(m.group(1), "unknown");
                    }
                    return new String[]{key, value};
                })
                .collect(Collectors.toMap(p -> p[0], p -> p[1]));
    }

    private List<Dependency> getDependencies(int repoId, List<Map<String, String>> depMaps) {
        List<Dependency> dependencyList = new ArrayList<>();
        for (Map<String, String> depMap : depMaps) {
            String content;
            try {
                content = objectMapper.writeValueAsString(depMap);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                content = "{\"status\": \"Parse error\"}";
            }
            String groupId = depMap.get("groupId");
            String artifactId = depMap.get("artifactId");
            Dependency dependency = Dependency.builder()
                    .repoId(repoId)
                    .mvnRepoId(groupId + "#" + artifactId)
                    .version(depMap.get("version") == null ? "unknown" : depMap.get("version"))
                    .content(content)
                    .jtsTimestamp(LocalDateTime.now())
                    .build();
            dependencyList.add(dependency);
        }
        return dependencyList;
    }

    private boolean isMavenRepoOverdue(String groupId, String artifactId) {
        MavenRepo mavenRepo = mavenRepoService.getById(groupId + "#" + artifactId);
        if (mavenRepo == null) {
            return true;
        }
        return Duration.between(LocalDateTime.now(), mavenRepo.getJtsTimestamp()).toMinutes() > REFRESH_MAVEN_REPO;
    }
}
