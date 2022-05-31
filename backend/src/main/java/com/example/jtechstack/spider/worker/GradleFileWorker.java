package com.example.jtechstack.spider.worker;


import com.example.jtechstack.entity.Dependency;
import com.example.jtechstack.entity.MavenRepo;
import com.example.jtechstack.entity.Repository;
import com.example.jtechstack.service.MavenRepoService;
import com.example.jtechstack.service.RepositoryService;
import com.example.jtechstack.spider.PageWorker;
import com.example.jtechstack.spider.common.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.jtechstack.spider.common.SpiderParam.*;

@Component
public class GradleFileWorker implements PageWorker {

    private static final Pattern POM_FILE_URL = Pattern.compile("https://raw\\.githubusercontent\\.com/.*/build\\.gradle");

    private static final Pattern DEP_PATTERN = Pattern.compile("dependencies\\s*\\{[\\s\\S]*'([^:\\s]+):([^:\\s]+):([^:\\s]+)'[\\s\\S]*}");

    private static final Logger logger = LoggerFactory.getLogger(GradleFileWorker.class);

    private final MavenRepoService mavenRepoService;
    private final RepositoryService repositoryService;

    public GradleFileWorker(MavenRepoService mavenRepoService, RepositoryService repositoryService) {
        this.mavenRepoService = mavenRepoService;
        this.repositoryService = repositoryService;
    }

    @Override
    public Pattern getPagePattern() {
        return POM_FILE_URL;
    }

    @Override
    public void process(Page page) {
        logger.info("Start process " + page.getRequest().getUrl());

        int repoId = page.getRequest().getExtra(REPO_ID);

        String content = page.getRawText();

        List<Dependency> dependencyList = new ArrayList<>();

        Matcher m = DEP_PATTERN.matcher(content);
        while (m.find()) {
            String groupId = m.group(1);
            String artifactId = m.group(2);
            Dependency dependency = Dependency.builder()
                    .repoId(repoId)
                    .mvnRepoId(groupId + "#" + artifactId)
                    .version(m.group(3))
                    .content(content)
                    .jtsTimestamp(LocalDateTime.now())
                    .build();
            dependencyList.add(dependency);
        }

        repositoryService.updateById(Repository.builder()
                .id(repoId)
                .management("Gradle")
                .build());

        /* add target page */

        for (Dependency dep : dependencyList) {
            String[] arr = dep.getMvnRepoId().split("#");
            String groupId = arr[0], artifactId = arr[1];
            if (isMavenRepoOverdue(groupId, artifactId)) {
                String mavenSearchUrl = String.format("https://mvnrepository.com/artifact/%s/%s", groupId, artifactId);
                page.addTargetRequest(RequestUtil.create(mavenSearchUrl)
                        .putExtra(P_USE_CURL, true)
                        .setPriority(PRIORITY_MVN_REPO));
                logger.info("Gradle file processed. Add target {}", mavenSearchUrl);
            } else {
                logger.info("Maven repo {} {} doesn't need to update", groupId, artifactId);
            }
        }
    }

    private boolean isMavenRepoOverdue(String groupId, String artifactId) {
        MavenRepo mavenRepo = mavenRepoService.getById(groupId + "#" + artifactId);
        if (mavenRepo == null) {
            return true;
        }
        return Duration.between(LocalDateTime.now(), mavenRepo.getJtsTimestamp()).toMinutes() > REFRESH_MAVEN_REPO;
    }
}
