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
public class MavenRepoWorker implements PageWorker {

    private static final Pattern MAVEN_REPO = Pattern.compile("https://mvnrepository\\.com/artifact/[^/]*/[^/]*");

    private static final Logger logger = LoggerFactory.getLogger(MavenRepoWorker.class);

    @Override
    public Pattern getPagePattern() {
        return MAVEN_REPO;
    }

    @Override
    public void process(Page page) {
        System.out.println("In MavenRepoWorker process");
        System.out.println(page.getRawText());


    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
    }
}
