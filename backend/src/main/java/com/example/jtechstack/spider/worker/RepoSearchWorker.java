package com.example.jtechstack.spider.worker;


import com.example.jtechstack.entity.Repository;
import com.example.jtechstack.service.RepositoryService;
import com.example.jtechstack.spider.PageWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.time.LocalDateTime;
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
    public void process(Page page) {
        logger.info("Process page " + page.getRequest().getUrl());
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
