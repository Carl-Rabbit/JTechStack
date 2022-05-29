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

    private static final String MAVEN_REPO_OBJ = "maven_repo_obj";

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

        mrb.groupId(doc.selectFirst("#maincontent > div.breadcrumb > a:nth-child(2)").text());

        String[] urlArray = page.getRequest().getUrl().split("/");
        mrb.artifactId(urlArray[urlArray.length - 1].trim());

        mrb.description(doc.selectFirst("#maincontent > div.im > div.im-description").text());

        String[] licenses = doc.select("span.b.lic")
                .stream()
                .map(Element::text)
                .toArray(String[]::new);
        mrb.license(Arrays.toString(licenses));

        mrb.imgUrl("https://mvnrepository.com" + doc.selectFirst(".im-logo").attr("src"));

        String[] categories = doc.select("a.b.c")
                .stream()
                .map(Element::text)
                .toArray(String[]::new);
        mrb.categories(Arrays.toString(categories));

        String[] tags = doc.select("a.b.tag")
                .stream()
                .map(Element::text)
                .toArray(String[]::new);
        mrb.tags(Arrays.toString(tags));

        String usedByContent = doc.select("#maincontent > table > tbody > tr:last-child > td").text();
        mrb.usedBy(Integer.parseInt(usedByContent.replace("artifacts", "").replaceAll(",", "").trim()));

        List<Map<String, Object>> versions = doc
                .select("#snippets > div > div > div > table.grid.versions > tbody")
                .stream()
                .map(el -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("version", el.selectFirst("a.vbtn.release").text());
                    if (el.selectFirst("a.vuln") != null) {
                        String vuln = el.selectFirst("a.vuln").text().replace("vulnerability", "");
                        map.put("vuln", Integer.parseInt(vuln));
                    }
                    String usages = el.selectFirst("tr > td:nth-child(5) > a").text();
                    map.put("usages", Integer.parseInt(usages.replaceAll(",", "").trim()));
                    map.put("date", el.selectFirst("tr > td:nth-child(6)").text());
                    return map;
                })
                .collect(Collectors.toList());
        mrb.versions(objectMapper.writeValueAsString(versions));

        mrb.jtsTimestamp(LocalDateTime.now());

        page.putField(MAVEN_REPO_OBJ, mrb.build());
    }

    @Override
    public boolean checkOverdue(ResultItems resultItems) {
        return false;
    }

    @Override
    public void save(ResultItems resultItems, Task task) {
        MavenRepo mavenRepo = resultItems.get(MAVEN_REPO_OBJ);
        UpdateWrapper<MavenRepo> uw = new UpdateWrapper<MavenRepo>()
                .allEq(new HashMap<String, String>() {{
                    this.put("group_id", mavenRepo.getGroupId());
                    this.put("artifact_id", mavenRepo.getArtifactId());
                }});
        boolean updated = mavenRepoService.update(mavenRepo, uw);
        if (updated) {
            logger.info("Update maven repo {} {} success",
                    mavenRepo.getGroupId(), mavenRepo.getArtifactId());
        } else {
            logger.warn("Update maven repo {} {} fail",
                    mavenRepo.getGroupId(), mavenRepo.getArtifactId());
        }
    }
}
