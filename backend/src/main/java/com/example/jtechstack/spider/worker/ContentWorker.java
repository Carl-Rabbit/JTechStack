package com.example.jtechstack.spider.worker;


import com.example.jtechstack.entity.Repository;
import com.example.jtechstack.service.RepositoryService;
import com.example.jtechstack.spider.PageWorker;
import com.example.jtechstack.spider.common.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.example.jtechstack.spider.common.SpiderParam.PRIORITY_MANAGEMENT_FILE;
import static com.example.jtechstack.spider.common.SpiderParam.REPO_ID;

@Component
public class ContentWorker implements PageWorker {

    private static final Pattern CONTENT_URL = Pattern.compile("https://api.github.com/repos/[^/].*/[^/].*/contents.*");

    private static final Logger logger = LoggerFactory.getLogger(ContentWorker.class);

    private final RepositoryService repositoryService;

    public ContentWorker(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public Pattern getPagePattern() {
        return CONTENT_URL;
    }

    @Override
    public void process(Page page) {
        int repoId = page.getRequest().getExtra(REPO_ID);

        ObjectMapper mapper = new ObjectMapper();

        JsonNode root;
        try {
            root = mapper.readTree(page.getRawText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        Map<String, JsonNode> fileNodeMap = new HashMap<>();

        for (JsonNode item : root) {
            String path = item.get("path").asText();
            fileNodeMap.put(path, item);
        }

        if (fileNodeMap.containsKey("pom.xml")) {
            JsonNode item = fileNodeMap.get("pom.xml");
            String downloadUrl = item.get("download_url").asText();
            page.addTargetRequest(RequestUtil.createWithAuth(downloadUrl)
                    .putExtra(REPO_ID, repoId)
                    .setPriority(PRIORITY_MANAGEMENT_FILE));
            logger.info("Add target " + downloadUrl);

        } else if (fileNodeMap.containsKey("build.gradle") && fileNodeMap.containsKey("app")) {
            JsonNode item = fileNodeMap.get("app");
            String downloadUrl = item.get("url").asText();
            page.addTargetRequest(RequestUtil.createWithAuth(downloadUrl)
                    .putExtra(REPO_ID, repoId)
                    .setPriority(PRIORITY_MANAGEMENT_FILE));
            logger.info("Add target " + downloadUrl);

        } else if (fileNodeMap.containsKey("app/build.gradle")) {
            JsonNode item = fileNodeMap.get("app/build.gradle");
            String downloadUrl = item.get("download_url").asText();
            page.addTargetRequest(RequestUtil.createWithAuth(downloadUrl)
                    .putExtra(REPO_ID, repoId)
                    .setPriority(PRIORITY_MANAGEMENT_FILE));
            logger.info("Add target " + downloadUrl);

        } else if (fileNodeMap.containsKey("build.gradle")) {
            JsonNode item = fileNodeMap.get("build.gradle");
            String downloadUrl = item.get("download_url").asText();
            page.addTargetRequest(RequestUtil.createWithAuth(downloadUrl)
                    .putExtra(REPO_ID, repoId)
                    .setPriority(PRIORITY_MANAGEMENT_FILE));
            logger.info("Add target " + downloadUrl);
        }
    }
}
