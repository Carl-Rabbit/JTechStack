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

@Component
public class MainPageProcessor implements PageProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MainPageProcessor.class);

    private final Site site = Site.me().setRetryTimes(3).setSleepTime(10000);
//            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.53")
//            .addHeader("authorization", "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");

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
