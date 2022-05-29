package com.example.jtechstack.spider.worker;


import com.example.jtechstack.spider.PageWorker;
import com.example.jtechstack.utils.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.util.regex.Pattern;

@Component
public class ContentWorker implements PageWorker {

    private static final Pattern CONTENT_URL = Pattern.compile("https://api.github.com/repos/[^/].*/[^/].*/contents");

    private static final Logger logger = LoggerFactory.getLogger(ContentWorker.class);

    @Override
    public Pattern getPagePattern() {
        return CONTENT_URL;
    }

    @Override
    public void process(Page page) {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode root;
        try {
            root = mapper.readTree(page.getRawText());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return;
        }

        for (JsonNode item : root) {
            String filename = item.get("name").asText();
            if (filename.matches("^(?i)pom.xml$")
//                    || filename.matches("^(?i)readme.*\\.md$")
//                    || filename.matches("^(?i)build.gradle$")
            ) {
                String downloadUrl = item.get("download_url").asText();
                page.addTargetRequest(RequestUtil.create(downloadUrl));
                logger.info("Add target " + downloadUrl);
            }
        }
    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
        // do nothing
    }
}
