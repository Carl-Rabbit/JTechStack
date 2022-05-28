package com.example.jtechstack.spider;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

@Component
public class MainPageProcessor implements PageProcessor {

    private final Site site = Site.me().setRetryTimes(3).setSleepTime(1000)
            .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.4951.64 Safari/537.36 Edg/101.0.1210.53")
            .addHeader("authorization", "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");

    private List<PageWorker> workers;

    public void setWorkers(List<PageWorker> workers) {
        this.workers = workers;
    }

    @SneakyThrows
    @Override
    public void process(Page page) {
        for (PageWorker worker : workers) {
            if (worker.getPagePattern() == null) {
                continue;
            }
            if (!page.getRequest().getUrl().matches(worker.getPagePattern().toString())) {
                continue;
            }

            worker.process(page);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }
}
