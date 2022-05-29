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
            "https://api.github.com/repos/macrozheng/mall/contents",
//            "https://api.github.com/repos/GoogleContainerTools/jib/contents",
//            "https://raw.githubusercontent.com/macrozheng/mall/master/pom.xml",
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
            MavenSearchWorker mavenSearchWorker
    ) {
        this.mainPageProcessor = mainPageProcessor;
        this.mainPipeline = mainPipeline;

        workers = new ArrayList<>();
        workers.add(repoSearchWorker);
        workers.add(contentWorker);
        workers.add(pomFileWorker);
        workers.add(gradleFileWorker);
        workers.add(mavenSearchWorker);

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
    }

    public void start() {
        this.spider.start();
    }

    public void stop() {
        this.spider.stop();
    }

    private static String proxyList = "39.130.150.42:80\n" +
            "43.255.113.232:80\n" +
            "51.91.157.66:80\n" +
            "106.75.22.218:80\n" +
            "103.181.72.169:80\n" +
            "124.193.110.231:80\n" +
            "218.202.1.58:80\n" +
            "189.198.250.253:80\n" +
            "23.97.65.112:80\n" +
            "3.211.17.212:80\n" +
            "103.115.14.116:80\n" +
            "138.68.143.131:80\n" +
            "108.166.183.204:80\n" +
            "104.160.189.3:80\n" +
            "83.40.151.94:80\n" +
            "182.70.121.249:80\n" +
            "91.229.114.63:80\n" +
            "54.217.76.59:80\n" +
            "101.33.80.144:80\n" +
            "103.121.38.138:80\n" +
            "39.130.150.44:80\n" +
            "220.88.6.12:80\n" +
            "125.22.61.90:80\n" +
            "125.22.61.92:80\n" +
            "8.208.91.118:80\n" +
            "80.81.243.26:80\n" +
            "103.112.150.220:80\n" +
            "91.244.66.174:80\n" +
            "183.181.164.210:80\n" +
            "220.88.6.1:80\n" +
            "68.183.200.41:80\n" +
            "51.75.206.209:80\n" +
            "129.213.183.152:80\n" +
            "75.101.218.120:80\n" +
            "167.71.212.154:80\n" +
            "47.89.253.218:80\n" +
            "37.223.29.196:80\n" +
            "35.246.142.152:80\n" +
            "34.122.187.196:80\n" +
            "93.188.162.47:80\n" +
            "103.20.204.104:80\n" +
            "50.16.33.219:80\n" +
            "20.239.2.157:80\n" +
            "20.239.2.157:80\n" +
            "39.130.150.43:80\n" +
            "202.133.91.2:80\n" +
            "217.91.176.69:80\n" +
            "103.117.192.14:80\n" +
            "165.227.188.89:80\n" +
            "182.61.201.201:80\n" +
            "106.14.255.124:80\n" +
            "103.148.72.192:80\n" +
            "154.85.58.149:80\n" +
            "154.85.58.149:80\n" +
            "129.226.162.177:80\n" +
            "14.139.120.231:80\n" +
            "149.54.11.74:80\n" +
            "201.144.210.221:80\n" +
            "195.201.34.206:80\n" +
            "216.254.142.34:80\n" +
            "1.186.85.38:80\n" +
            "59.21.84.108:80\n" +
            "220.88.6.25:80\n" +
            "220.88.6.19:80\n" +
            "87.27.62.235:80\n" +
            "117.54.114.96:80\n" +
            "172.105.197.49:80\n" +
            "154.12.243.75:80\n" +
            "23.94.143.167:80\n" +
            "206.81.31.215:80\n" +
            "208.113.134.223:80\n" +
            "34.95.191.125:80\n" +
            "138.197.142.249:80\n" +
            "134.209.241.12:80\n" +
            "182.74.183.226:80\n" +
            "46.38.242.194:80\n" +
            "39.99.54.91:80\n" +
            "197.243.20.178:80\n" +
            "219.246.65.55:80\n" +
            "139.155.182.3:80\n" +
            "137.74.168.93:80\n" +
            "183.181.168.48:80\n" +
            "3.109.193.95:80\n" +
            "208.67.183.240:80\n" +
            "203.110.240.166:80\n" +
            "68.183.92.86:80\n" +
            "190.84.75.133:80\n" +
            "104.211.157.219:80\n" +
            "206.125.47.4:80\n" +
            "140.227.127.205:80\n" +
            "45.40.240.71:80\n" +
            "200.54.22.74:80\n" +
            "202.152.39.146:80\n" +
            "54.175.197.235:80\n" +
            "117.54.114.97:80\n" +
            "8.142.142.250:80\n" +
            "159.203.13.121:80\n" +
            "162.144.233.16:80\n" +
            "77.68.12.217:80\n" +
            "107.179.33.13:80";
}
