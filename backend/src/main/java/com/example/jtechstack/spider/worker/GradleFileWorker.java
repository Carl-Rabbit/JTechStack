package com.example.jtechstack.spider.worker;


import com.example.jtechstack.spider.PageWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.util.regex.Pattern;

@Component
public class GradleFileWorker implements PageWorker {

    private static final Pattern POM_FILE_URL = Pattern.compile("https://raw\\.githubusercontent\\.com/[^/].*/[^/].*/[^/].*/build\\.gradle");

    private static final Logger logger = LoggerFactory.getLogger(GradleFileWorker.class);

    @Override
    public Pattern getPagePattern() {
        return POM_FILE_URL;
    }

    @Override
    public void process(Page page) {
        logger.info("Start process " + page.getRequest().getUrl());
        // read properties
//        page.getRawText()
    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
    }
}
