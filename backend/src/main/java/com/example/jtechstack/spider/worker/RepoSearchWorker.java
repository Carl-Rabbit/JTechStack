package com.example.jtechstack.spider.worker;


import com.example.jtechstack.entity.Repository;
import com.example.jtechstack.service.RepositoryService;
import com.example.jtechstack.spider.PageWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.selector.Json;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.regex.Pattern;

@Component
public class RepoSearchWorker implements PageWorker {

    private static final Pattern REPO_SEARCH_URL = Pattern.compile("https://api.github.com/search/repositories.*");

    private static final Logger logger = LoggerFactory.getLogger(RepoSearchWorker.class);

    private final RepositoryService repositoryService;

    public RepoSearchWorker(RepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public Pattern getPagePattern() {
        return REPO_SEARCH_URL;
    }

    @Override
    public void process(Page page) throws JsonProcessingException {
        logger.info("Process page " + page.getRequest().getUrl());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(page.getRawText());
        JsonNode itemsNode = rootNode.get("items");
//        JsonNodeIterator jsonNodeIterator = new JsonNodeIterator();
        ArrayList<Repository> repoList = new ArrayList<>();
        ArrayList<String> repoAddressList = new ArrayList<>();
        for (int i = 0; i < itemsNode.size(); i++) {
//            System.out.println(itemsNode.get(i).findValue("id").asText());
//            System.out.println(itemsNode.get(i).asInt());
//addtargetrequest

            repoList.add(Repository.builder()
                    .id(itemsNode.get(i).findValue("id").asInt())
                    .name(itemsNode.get(i).findValue("name").asText())
                    .fullName(itemsNode.get(i).findValue("full_name").asText())
                    .ownerId(itemsNode.get(i).findValue("owner").get("id").asInt())
                    .createdAt(itemsNode.get(i).findValue("created_at").asText())
                    .updatedAt(itemsNode.get(i).findValue("updated_at").asText())
                    .pushedAt(itemsNode.get(i).findValue("pushed_at").asText())
                    .size(itemsNode.get(i).findValue("size").asInt())
                    .forks(itemsNode.get(i).findValue("forks").asInt())
                    .openIssues(itemsNode.get(i).findValue("open_issues").asInt())
                    .watchers(itemsNode.get(i).findValue("watchers").asInt())
                    .licenseKey(itemsNode.get(i).findValue("license").isNull() ? null : itemsNode.get(i).findValue("license").get("key").asText())
                    .allowForking(itemsNode.get(i).findValue("allow_forking").asBoolean())
                    .isTemplate(itemsNode.get(i).findValue("is_template").asBoolean())
                    .topics(itemsNode.get(i).findValue("topics").toString())
                    .content(itemsNode.get(i).toString())
                    .build());
        }

// addField
    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
        Repository testRepo = Repository.builder()
                .id(0)
                .name("testRepo")
                .jtsTimestamp(LocalDateTime.now())
                .build();
        repositoryService.saveOrUpdate(testRepo);
    }
}
