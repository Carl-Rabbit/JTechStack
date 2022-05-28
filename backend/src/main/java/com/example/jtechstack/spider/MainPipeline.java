package com.example.jtechstack.spider;

import com.sun.jndi.toolkit.url.UrlUtil;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.List;

@Component
public class MainPipeline implements Pipeline {

    private List<PageWorker> workers;

    public void setWorkers(List<PageWorker> workers) {
        this.workers = workers;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        for (PageWorker worker : workers) {
            if (worker.getPagePattern() == null) {
                continue;
            }
            if (!resultItems.getRequest().getUrl().matches(worker.getPagePattern().toString())) {
                continue;
            }
            if (worker.checkOverdue(resultItems)) {
                continue;
            }
            worker.save(resultItems, task);
        }
    }
}
