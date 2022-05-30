package com.example.jtechstack.spider;

import com.example.jtechstack.spider.conponent.MainPageProcessor;
import com.example.jtechstack.spider.conponent.MyHttpClientDownloader;
import com.example.jtechstack.spider.worker.*;

import com.example.jtechstack.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.SpiderListener;


import java.util.*;

import static com.example.jtechstack.spider.SpiderParam.ROOT_URL_LIST;
import static com.example.jtechstack.spider.SpiderParam.THREAD_CNT;

@Component
public class SpiderManager {
    private static final Logger logger = LoggerFactory.getLogger(RepoSearchWorker.class);

    private Spider spider;

    private final MainPageProcessor mainPageProcessor;
    private final List<PageWorker> workers;
    private final List<SpiderListener> spiderListeners;

    public SpiderManager(
            MainPageProcessor mainPageProcessor,

            RepoSearchWorker repoSearchWorker,
            ContentWorker contentWorker,
            PomFileWorker pomFileWorker,
            GradleFileWorker gradleFileWorker,
            MavenRepoWorker mavenRepoWorker,
            ContributorWorker contributorWorker
    ) {
        this.mainPageProcessor = mainPageProcessor;

        workers = new ArrayList<>();
        workers.add(repoSearchWorker);
        workers.add(contentWorker);
        workers.add(pomFileWorker);
        workers.add(gradleFileWorker);
        workers.add(mavenRepoWorker);
        workers.add(contributorWorker);

        this.mainPageProcessor.setWorkers(workers);

        this.spiderListeners = new ArrayList<>();
        spiderListeners.add(new MySpiderListener());

        this.initSpider();
    }

    public void initSpider() {
        if (this.spider != null && this.spider.getStatus().equals(Spider.Status.Running)) {
            this.stop();
        }
        this.spider = Spider.create(mainPageProcessor)
                .addUrl(ROOT_URL_LIST)
                .setSpiderListeners(spiderListeners)
                .thread(THREAD_CNT);
        this.spider.addRequest(Arrays.stream(ROOT_URL_LIST).map(RequestUtil::create).toArray(Request[]::new));

        this.spider.setDownloader(new MyHttpClientDownloader());
    }

    public void start() {
        this.spider.start();
    }

    public void stop() {
        this.spider.stop();
    }

    private class MySpiderListener implements SpiderListener {
        @Override
        public void onSuccess(Request request) {

        }

        @Override
        public void onError(Request request) {

        }
    }
}
