package com.example.jtechstack.spider;

import com.example.jtechstack.spider.conponent.MainPageProcessor;
import com.example.jtechstack.spider.conponent.MyHttpClientDownloader;
import com.example.jtechstack.spider.worker.*;

import com.example.jtechstack.spider.common.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.scheduler.PriorityScheduler;


import java.util.*;

import static com.example.jtechstack.spider.common.SpiderParam.*;

@Component
public class SpiderManager {
    private static final Logger logger = LoggerFactory.getLogger(RepoSearchWorker.class);

    private Spider spider;

    private final MainPageProcessor mainPageProcessor;
    private final List<PageWorker> workers;
    private final MyHttpClientDownloader.DownloaderListener downloaderListener;

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

        this.downloaderListener = new MyDownloaderListener();

        this.initSpider();
    }

    public void initSpider() {
        if (this.spider != null && this.spider.getStatus().equals(Spider.Status.Running)) {
            this.stop();
        }

        this.spider = Spider.create(mainPageProcessor)
                .setDownloader(new MyHttpClientDownloader(this.downloaderListener))
//                .setScheduler(new FileCacheQueueScheduler(CACHE_PATH))
                .setScheduler(new PriorityScheduler())
                .thread(THREAD_CNT);

        for (String url : ROOT_URL_LIST) {
            Request r;
            if (url.contains("https://mvnrepository")) {
                r = RequestUtil.create(url);
                r.putExtra(P_USE_CURL, true);
            } else {
                r = RequestUtil.createWithAuth(url);
            }
            r.setExtras(ROOT_PAGE_EXTRA);
            r.setPriority(PRIORITY_ROOT);
            spider.addRequest(r);
        }
    }

    public void start() {
        this.spider.start();
    }

    public void stop() {
        this.spider.stop();
    }

    private class MyDownloaderListener extends MyHttpClientDownloader.DownloaderListener {
        @Override
        protected void onCodeError(Page page) {
            logger.warn("Code error {} . code={}, status_line={}",
                    page.getUrl(), page.getStatusCode(), page.getRequest().getExtra(P_STATUS_LINE));
            if (page.getRequest().getUrl().contains("https://api.github.com/search/repositories")) {
                retryRequest(page.getRequest());
            }
        }

        @Override
        protected void onError(Page page, Request request, Exception e) {
            this.retryRequest(request);
            logger.error("Request error {}", request.getUrl());
            e.printStackTrace();
            logger.info("Retry request {}", request.getUrl());
        }

        private void retryRequest(Request request) {
            spider.addRequest(request);
        }
    }
}
