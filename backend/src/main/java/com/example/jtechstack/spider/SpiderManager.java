package com.example.jtechstack.spider;

import com.example.jtechstack.spider.worker.*;

import com.example.jtechstack.utils.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;


import java.util.*;

@Component
public class SpiderManager {
    private static final int THREAD_CNT = 5;
    private static final String[] ROOT_URL_LIST = {
//            "https://api.github.com/search/repositories?q=language:java&sort=stars",
            // only for test
//            "https://api.github.com/repos/doocs/advanced-java/contributors",
//            "https://api.github.com/repos/macrozheng/mall/contents",
//            "https://api.github.com/repos/GoogleContainerTools/jib/contents",
            "https://raw.githubusercontent.com/macrozheng/mall/master/pom.xml",
//            "https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-actuator",
    };

    private static final Logger logger = LoggerFactory.getLogger(RepoSearchWorker.class);

    private Spider spider;

    private final MainPageProcessor mainPageProcessor;
    private final MainPipeline mainPipeline;
    private final List<PageWorker> workers;

    public SpiderManager(
            MainPageProcessor mainPageProcessor,
            MainPipeline mainPipeline,

            RepoSearchWorker repoSearchWorker,
            ContentWorker contentWorker,
            PomFileWorker pomFileWorker,
            GradleFileWorker gradleFileWorker,
            MavenRepoWorker mavenRepoWorker,
            ContributorWorker contributorWorker
    ) {
        this.mainPageProcessor = mainPageProcessor;
        this.mainPipeline = mainPipeline;

        workers = new ArrayList<>();
        workers.add(repoSearchWorker);
        workers.add(contentWorker);
        workers.add(pomFileWorker);
        workers.add(gradleFileWorker);
        workers.add(mavenRepoWorker);
        workers.add(contributorWorker);

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
                .addUrl(ROOT_URL_LIST)
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
}
