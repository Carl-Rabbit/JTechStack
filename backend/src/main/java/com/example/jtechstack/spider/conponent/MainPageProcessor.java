package com.example.jtechstack.spider.conponent;

import com.example.jtechstack.spider.PageWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.Arrays;
import java.util.List;

import static com.example.jtechstack.spider.common.SpiderParam.RETRY_TIMES;
import static com.example.jtechstack.spider.common.SpiderParam.SLEEP_TIME;

@Component
public class MainPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MainPageProcessor.class);

    private final Site site = Site.me().setRetryTimes(RETRY_TIMES).setSleepTime(SLEEP_TIME);

    private List<PageWorker> workers;

    public void setWorkers(List<PageWorker> workers) {
        this.workers = workers;
    }

    @Override
    public void process(Page page) {
        boolean hasProcessed = false;
        for (PageWorker worker : workers) {
            if (worker.getPagePattern() == null) {
                continue;
            }
            if (!worker.getPagePattern().matcher(page.getRequest().getUrl()).matches()) {
                continue;
            }

            try {
                worker.process(page);
            } catch (Exception e) {
                logger.error("Error when processing page " + page.getRequest().getUrl()
                        + "\n Nested error is " + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
            hasProcessed = true;
        }
        if (!hasProcessed) {
            logger.warn("No matched worker for page " + page.getRequest().getUrl());
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
