package com.example.jtechstack.spider.conponent;

import com.example.jtechstack.spider.PageWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.util.Arrays;
import java.util.List;

@Component
public class MainPipeline implements Pipeline {

    private static final Logger logger = LoggerFactory.getLogger(MainPipeline.class);

    private List<PageWorker> workers;

    public void setWorkers(List<PageWorker> workers) {
        this.workers = workers;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        boolean hasProcessed = false;

        for (PageWorker worker : workers) {
            if (worker.getPagePattern() == null) {
                continue;
            }
            if (!worker.getPagePattern().matcher(resultItems.getRequest().getUrl()).matches()) {
                continue;
            }
            if (worker.checkOverdue(resultItems)) {
                continue;
            }

            try {
                worker.save(resultItems, task);
            } catch (Exception e) {
                logger.error("Error when saving page " + resultItems.getRequest().getUrl()
                        + "\n Nested error is " + Arrays.toString(e.getStackTrace()));
                e.printStackTrace();
            }
            hasProcessed = true;
        }

        if (!hasProcessed) {
            logger.warn("Result set is useless. " + resultItems.getRequest().getUrl());
        }
    }
}
