package com.example.jtechstack.spider.worker;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.jtechstack.entity.Contributor;
import com.example.jtechstack.entity.User;
import com.example.jtechstack.service.ContributorService;
import com.example.jtechstack.service.UserService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import static com.example.jtechstack.spider.SpiderParam.REPO_ID;

@Component
public class ContributorWorker implements PageWorker {

    private static final Pattern REPO_SEARCH_URL = Pattern.compile("https://api\\.github\\.com/repos/[^/]*/[^/]*/contributors");

    private static final Logger logger = LoggerFactory.getLogger(ContributorWorker.class);

    private final UserService userService;
    private final ContributorService contributorService;

    public ContributorWorker(UserService userService, ContributorService contributorService) {
        this.userService = userService;
        this.contributorService = contributorService;
    }

    @Override
    public Pattern getPagePattern() {
        return REPO_SEARCH_URL;
    }

    @Override
    public void process(Page page) throws JsonProcessingException {
        int repoId = page.getRequest().getExtra(REPO_ID);
        logger.info("Process repo contributors, repo_id={}", repoId);

        logger.info("Process page " + page.getRequest().getUrl());
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(page.getRawText());
        ArrayList<User> userList = new ArrayList<>();
        ArrayList<Contributor> contributorsList = new ArrayList<>();

        for (int i = 0; i < rootNode.size(); i++) {
            userList.add(User.builder()
                    .id(rootNode.get(i).findValue("id").asInt())
                    .login(rootNode.get(i).findValue("login").asText())
                    .content(rootNode.get(i).toString())
                    .build());
            contributorsList.add(Contributor.builder()
                    .repoId(repoId)
                    .userId(rootNode.get(i).findValue("id").asInt())
                    .contributions(rootNode.get(i).findValue("contributions").asInt())
                    .content(rootNode.get(i).toString())
                    .build());
        }

        userService.saveOrUpdateBatch(userList);

        contributorsList.parallelStream().forEach(c -> {
            UpdateWrapper<Contributor> uw = new UpdateWrapper<Contributor>()
                    .allEq(new HashMap<String, Integer>(){{
                        this.put("repo_id", c.getRepoId());
                        this.put("user_id", c.getUserId());
                    }});
            contributorService.saveOrUpdate(c, uw);
        });
    }
}
