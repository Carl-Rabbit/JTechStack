package com.example.jtechstack.spider.worker;


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
import java.util.regex.Pattern;

@Component
public class ContributorWorker implements PageWorker {

    private static final Pattern REPO_SEARCH_URL = Pattern.compile("https://api\\.github\\.com/repos/[^/]*/[^/]*/contributors");

    private static final Logger logger = LoggerFactory.getLogger(ContributorWorker.class);

    private final UserService userService;
    private final ContributorService contributorService;

    private static final String USER_LIST = "user_list";
    private static final String CONTRIBUTION_LIST = "contribution_list";

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
        logger.info("repo_id={}", (Integer) page.getRequest().getExtra("repo_id"));

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
                    .repoId(page.getRequest().getExtra("repo_id"))
                    .userId(rootNode.get(i).findValue("id").asInt())
                    .contributions(rootNode.get(i).findValue("contributions").asInt())
                    .content(rootNode.get(i).toString())
                    .build());
        }

        page.putField(USER_LIST, userList);
        page.putField(CONTRIBUTION_LIST, contributorsList);

    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
        userService.saveOrUpdateBatch(resultItems.get(USER_LIST));
        contributorService.saveOrUpdateBatch(resultItems.get(CONTRIBUTION_LIST));
    }
}
