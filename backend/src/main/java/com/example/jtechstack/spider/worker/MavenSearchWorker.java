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
public class MavenSearchWorker implements PageWorker {

    private static final Pattern MAVEN_SEARCH_URL = Pattern.compile("https://mvnrepository\\.com/search\\?q=.*");

    private static final Logger logger = LoggerFactory.getLogger(MavenSearchWorker.class);

    @Override
    public Pattern getPagePattern() {
        return MAVEN_SEARCH_URL;
    }

    @Override
    public void process(Page page) {
        // read properties
//        page.getHtml().xpath("").all()
    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
    }
}
