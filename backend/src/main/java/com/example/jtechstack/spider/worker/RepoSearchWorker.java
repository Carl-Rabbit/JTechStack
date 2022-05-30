package com.example.jtechstack.spider.worker;


import com.example.jtechstack.entity.Repository;
import com.example.jtechstack.entity.User;
import com.example.jtechstack.service.RepositoryService;
import com.example.jtechstack.service.UserService;
import com.example.jtechstack.spider.PageWorker;
import com.example.jtechstack.spider.common.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;

import java.util.ArrayList;
import java.util.regex.Pattern;

import static com.example.jtechstack.spider.common.SpiderParam.*;

@Component
public class RepoSearchWorker implements PageWorker {

    private static final Pattern REPO_SEARCH_URL = Pattern.compile("https://api\\.github\\.com/search/repositories.*");

    private static final Logger logger = LoggerFactory.getLogger(RepoSearchWorker.class);

    private final RepositoryService repositoryService;
    private final UserService userService;

    public RepoSearchWorker(RepositoryService repositoryService, UserService userService) {
        this.repositoryService = repositoryService;
        this.userService = userService;
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
        ArrayList<Repository> repoList = new ArrayList<>();
        ArrayList<User> ownerList = new ArrayList<>();
        ArrayList<Request> repoAddressList = new ArrayList<>();

        for (int i = 0; i < itemsNode.size(); i++) {
            repoList.add(Repository.builder()
                    .id(itemsNode.get(i).findValue("id").asInt())
                    .name(itemsNode.get(i).findValue("name").asText())
                    .fullName(itemsNode.get(i).findValue("full_name").asText())
                    .ownerId(itemsNode.get(i).findValue("owner").get("id").asInt())
                    .createdAt(itemsNode.get(i).findValue("created_at").asText())
                    .updatedAt(itemsNode.get(i).findValue("updated_at").asText())
                    .pushedAt(itemsNode.get(i).findValue("pushed_at").asText())
                    .size(itemsNode.get(i).findValue("size").asInt())
                    .stars(itemsNode.get(i).findValue("stargazers_count").asInt())
                    .forks(itemsNode.get(i).findValue("forks").asInt())
                    .openIssues(itemsNode.get(i).findValue("open_issues").asInt())
                    .watchers(itemsNode.get(i).findValue("watchers").asInt())
                    .licenseKey(itemsNode.get(i).findValue("license").isNull() ? null : itemsNode.get(i).findValue("license").get("key").asText())
                    .allowForking(itemsNode.get(i).findValue("allow_forking").asBoolean())
                    .isTemplate(itemsNode.get(i).findValue("is_template").asBoolean())
                    .topics(itemsNode.get(i).findValue("topics").toString())
                    .content(itemsNode.get(i).toString())
                    .build());
            ownerList.add(User.builder()
                    .id(itemsNode.get(i).findValue("owner").get("id").asInt())
                    .login(itemsNode.get(i).findValue("owner").get("login").asText())
                    .content(itemsNode.get(i).findValue("owner").toString())
                    .build());

            String contentUrl = itemsNode.get(i).findValue("contents_url").asText().replace("/{+path}", "");
            repoAddressList.add(RequestUtil.createWithAuth(contentUrl)
                    .putExtra(REPO_ID, itemsNode.get(i).findValue("id").asInt())
                    .setPriority(PRIORITY_CONTENT));

            String contributorUrl = itemsNode.get(i).findValue("contributors_url").asText();
            repoAddressList.add(RequestUtil.createWithAuth(contributorUrl)
                    .putExtra(REPO_ID, itemsNode.get(i).findValue("id").asInt())
                    .setPriority(PRIORITY_CONTRIBUTOR));
        }

        repositoryService.saveOrUpdateBatch(repoList);
        userService.saveOrUpdateBatch(ownerList);

        repoAddressList.forEach(page::addTargetRequest);
    }
}
