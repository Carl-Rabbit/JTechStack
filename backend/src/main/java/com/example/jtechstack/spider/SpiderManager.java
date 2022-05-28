package com.example.jtechstack.spider;

import com.example.jtechstack.spider.worker.RepoSearchWorker;

import com.example.jtechstack.utils.UrlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SpiderManager {
    private static final int THREAD_CNT = 5;
    private static final String[] ROOT_URL_LIST = {
            "https://api.github.com/search/repositories?q=language:java&sort=stars",
    };

    private static final Logger logger = LoggerFactory.getLogger(RepoSearchWorker.class);

    private Spider spider;

    private final MainPageProcessor mainPageProcessor;
    private final MainPipeline mainPipeline;
    private final List<PageWorker> workers;

    public SpiderManager(MainPageProcessor mainPageProcessor, MainPipeline mainPipeline, RepoSearchWorker repoSearchWorker) {
        this.mainPageProcessor = mainPageProcessor;
        this.mainPipeline = mainPipeline;

        workers = new ArrayList<>();
        workers.add(repoSearchWorker);
        this.mainPageProcessor.setWorkers(workers);
        this.mainPipeline.setWorkers(workers);

        this.initSpider();
    }

    public void initSpider() {
        if (this.spider != null && this.spider.getStatus().equals(Spider.Status.Running)) {
            this.stop();
        }
        this.spider = Spider.create(this.mainPageProcessor)
                .addPipeline(this.mainPipeline)
                .addUrl(Arrays.stream(ROOT_URL_LIST).map(url -> UrlUtil.appendVersion(url, 0)).toArray(String[]::new))
                .thread(THREAD_CNT);
    }

    public void start() {
        this.spider.start();
    }

    public void stop() {
        this.spider.stop();
    }
}
