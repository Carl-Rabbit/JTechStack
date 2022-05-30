package com.example.jtechstack.spider.worker;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.jtechstack.entity.MavenRepo;
import com.example.jtechstack.service.MavenRepoService;
import com.example.jtechstack.spider.PageWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MavenRepoWorker implements PageWorker {

    private static final Pattern MAVEN_REPO = Pattern.compile("https://mvnrepository\\.com/artifact/[^/]*/[^/]*");

    private static final Logger logger = LoggerFactory.getLogger(MavenRepoWorker.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MavenRepoService mavenRepoService;

    public MavenRepoWorker(MavenRepoService mavenRepoService) {
        this.mavenRepoService = mavenRepoService;
    }

    @Override
    public Pattern getPagePattern() {
        return MAVEN_REPO;
    }

    @Override
    public void process(Page page) throws JsonProcessingException {
        logger.info("Start process " + page.getRequest().getUrl());

        Document doc = Jsoup.parse(page.getRawText());

        MavenRepo.MavenRepoBuilder mrb = MavenRepo.builder();

        mrb.name(doc.selectFirst("#maincontent > div.im > div.im-header > h2 > a").text());

        String groupId = doc.selectFirst("#maincontent > div.breadcrumb > a:nth-child(2)").text();
        mrb.groupId(groupId);

        String[] urlArray = page.getRequest().getUrl().split("/");
        String artifactId = urlArray[urlArray.length - 1].trim();
        mrb.artifactId(artifactId);

        mrb.id(groupId + "#" + artifactId);

        mrb.description(doc.selectFirst("#maincontent > div.im > div.im-description").text());

        String[] licenses = doc.select("span.b.lic")
                .stream()
                .map(Element::text)
                .toArray(String[]::new);
        mrb.license(objectMapper.writeValueAsString(licenses));

        mrb.imgUrl("https://mvnrepository.com" + doc.selectFirst(".im-logo").attr("src"));

        String[] categories = doc.select("a.b.c")
                .stream()
                .map(Element::text)
                .toArray(String[]::new);
        mrb.categories(objectMapper.writeValueAsString(categories));

        String[] tags = doc.select("a.b.tag")
                .stream()
                .map(Element::text)
                .toArray(String[]::new);
        mrb.tags(objectMapper.writeValueAsString(tags));

        String usedByContent = doc.select("#maincontent > table > tbody > tr:last-child > td").text();
        mrb.usedBy(Integer.parseInt(usedByContent.replace("artifacts", "").replaceAll(",", "").trim()));

        Element tableEl = doc.selectFirst("#snippets > div > div > div > table.grid.versions");
        List<Map<String, Object>> versions = tableEl
                .select("tbody > tr")
                .stream()
                .map(el -> {
                    int colCnt = el.select("td").size();
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("version", el.selectFirst("a.vbtn").text());
                    if (el.selectFirst("a.vuln") != null) {
                        String vuln = el.selectFirst("a.vuln").text()
                                .replace("vulnerability", "")
                                .trim();
                        map.put("vuln", Integer.parseInt(vuln));
                    }
                    String usages = el.selectFirst("td:nth-child(" + (colCnt-1) + ") > a").text();
                    map.put("usages", Integer.parseInt(usages.replaceAll(",", "").trim()));
                    map.put("date", el.selectFirst("td:nth-child(" + (colCnt) + ")").text());
                    return map;
                })
                .collect(Collectors.toList());
        mrb.versions(objectMapper.writeValueAsString(versions));

        mrb.jtsTimestamp(LocalDateTime.now());


        MavenRepo mavenRepo = mrb.build();

        boolean updated = mavenRepoService.saveOrUpdate(mavenRepo);
        if (updated) {
            logger.info("Update maven repo {} {} success",
                    mavenRepo.getGroupId(), mavenRepo.getArtifactId());
        } else {
            logger.warn("Update maven repo {} {} fail",
                    mavenRepo.getGroupId(), mavenRepo.getArtifactId());
        }
    }
}
