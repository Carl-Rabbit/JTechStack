package com.example.jtechstack.spider.worker;


import com.example.jtechstack.spider.PageWorker;
import com.example.jtechstack.spider.common.RequestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;

import java.util.regex.Pattern;

import static com.example.jtechstack.spider.common.SpiderParam.PRIORITY_POM;
import static com.example.jtechstack.spider.common.SpiderParam.REPO_ID;

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
        int repoId = page.getRequest().getExtra(REPO_ID);

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
                page.addTargetRequest(RequestUtil.createWithAuth(downloadUrl)
                        .putExtra(REPO_ID, repoId)
                        .setPriority(PRIORITY_POM));
                logger.info("Add target " + downloadUrl);
            }
        }
    }
}
