package com.example.jtechstack.spider.worker;


import com.example.jtechstack.spider.PageWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Task;

@Component
public class RepoSearchWorker implements PageWorker {

    private static final String REPO_SEARCH_URL = "https://api.github.com/search/repositories.*";

    private static final Logger logger = LoggerFactory.getLogger(RepoSearchWorker.class);

    @Override
    public String getPagePattern() {
        return REPO_SEARCH_URL;
    }

    @Override
    public void process(Page page) {
        logger.info("Process page " + page.getRequest().getUrl());
    }

    @Override
    public void save(Task task) {

    }
}
