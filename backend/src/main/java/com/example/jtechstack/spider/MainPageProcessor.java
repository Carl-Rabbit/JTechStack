package com.example.jtechstack.spider;

import com.example.jtechstack.utils.UrlUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

@Component
public class MainPageProcessor implements PageProcessor {

    private final Site site = Site.me().setRetryTimes(3).setSleepTime(1000);

    private List<PageWorker> workers;

    public void setWorkers(List<PageWorker> workers) {
        this.workers = workers;
    }

    @Override
    public void process(Page page) {
        for (PageWorker worker : workers) {
            if (worker.getPagePattern() == null) {
                continue;
            }
            String pattern = UrlUtil.appendVersionPattern(worker.getPagePattern().toString());
            if (!page.getRequest().getUrl().matches(pattern)) {
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
